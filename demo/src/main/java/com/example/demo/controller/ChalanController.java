package com.example.demo.controller;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.example.demo.dtos.UpdateBunchesDto;
import com.example.demo.model.Bunch;
import com.example.demo.model.Chalan;
import com.example.demo.model.Status;
import com.example.demo.model.User;
import com.example.demo.service.ChalanService;
import com.example.demo.service.UserService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chalans")
@CrossOrigin(origins = "*")
public class ChalanController {

    private final ChalanService service;
    private final UserService userService;

    public ChalanController(ChalanService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    // 🔐 helper
    private User getLoggedUser(Authentication auth) {
        return userService.findByMobile(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ✅ CREATE
    @PostMapping
    public Chalan create(@RequestBody Chalan chalan, Authentication auth) {
        if (chalan.getDate() == null) {
            chalan.setDate(LocalDate.now());
        }

        User user = getLoggedUser(auth);
        chalan.setUser(user);

        return service.create(chalan);
    }

    // ✅ GET ALL (USER ONLY)
    @GetMapping
    public List<Chalan> getAll(Authentication auth) {
        return service.findAllByUser(getLoggedUser(auth));
    }

    // ✅ GET BY STATUS
    @GetMapping("/status/{status}")
    public List<Chalan> getByStatus(
            @PathVariable String status,
            Authentication auth) {

        return service.findByStatusAndUser(
                Status.valueOf(status),
                getLoggedUser(auth)
        );
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Chalan> getById(
            @PathVariable String id,
            Authentication auth) {

        return service.findByIdAndUser(id, getLoggedUser(auth))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ TO MILL
    @PostMapping("/{id}/to-mill")
    public ResponseEntity<Chalan> toMill(
            @PathVariable String id,
            @RequestBody MillDto dto,
            Authentication auth) {

        return service.findByIdAndUser(id, getLoggedUser(auth))
                .map(c -> {
                    c.setUpdatedMeter(dto.getUpdatedMeter());
                    c.setMillName(dto.getMillName());
                    c.setColorCode(dto.getColorCode());
                    c.setStatus(Status.AT_MILL);
                    return ResponseEntity.ok(service.save(c));
                }).orElse(ResponseEntity.notFound().build());
    }

    // ✅ TO BORDER
    @PostMapping("/{id}/to-border")
    public ResponseEntity<Chalan> toBorder(
            @PathVariable String id,
            @RequestBody BorderDto dto,
            Authentication auth) {

        return service.findByIdAndUser(id, getLoggedUser(auth))
                .map(c -> {
                    c.setShopName(dto.getShopName());
                    c.setStatus(Status.AT_BORDER_CUTTING);
                    return ResponseEntity.ok(service.save(c));
                }).orElse(ResponseEntity.notFound().build());
    }

    // ✅ FILTER
    @PostMapping("/filter")
    public List<Chalan> filter(
            @RequestBody Map<String, String> filters,
            Authentication auth) {

        return service.filter(filters, getLoggedUser(auth));
    }

    // ✅ RETURN
    @PostMapping("/{id}/return")
    public ResponseEntity<Chalan> toReturn(
            @PathVariable String id,
            Authentication auth) {

        return service.findByIdAndUser(id, getLoggedUser(auth))
                .map(c -> {
                    c.setStatus(Status.RETURNED_TO_MAIN_SHOP);
                    return ResponseEntity.ok(service.save(c));
                }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/bunches")
    public ResponseEntity<?> updateBunches(
            @PathVariable String id,
            @RequestBody UpdateBunchesDto dto,
            Authentication auth) {

        return service.findByIdAndUser(id, getLoggedUser(auth))
                .map(c -> {

                    // ✅ Update rate
                    c.setRate(dto.getRate());

                    // ✅ Update bunches
                    List<Bunch> bunches = dto.getBunches();
                    c.setBunches(bunches);

                    // ✅ Recalculate total meter
                    double totalMeter = bunches.stream()
                            .mapToDouble(b -> b.getQty() * b.getMeter())
                            .sum();

                    c.setMeter(totalMeter);

                    service.save(c);
                    return ResponseEntity.ok(c);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    // ✅ PDF
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> pdf(
            @PathVariable String id,
            Authentication auth) {

        return service.findByIdAndUser(id, getLoggedUser(auth))
                .map(c -> {
                    try {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        Document doc = new Document();
                        PdfWriter.getInstance(doc, baos);
                        doc.open();

                        doc.add(new Paragraph("Chalan No: " + c.getChalanNo()));
                        doc.add(new Paragraph("Status: " + c.getStatus()));
                        doc.add(new Paragraph("Meter: " + c.getMeter()));

                        doc.close();

                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_PDF);
                        headers.setContentDispositionFormData(
                                "attachment",
                                "chalan-" + c.getChalanNo() + ".pdf"
                        );

                        return ResponseEntity.ok()
                                .headers(headers)
                                .body(baos.toByteArray());

                    } catch (Exception e) {
                        return ResponseEntity
                                .status(500)
                                .<byte[]>body(null);
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }


    // DTOs
    public static class MillDto {
        private double updatedMeter;
        private String millName;
        private String colorCode;
        public double getUpdatedMeter() { return updatedMeter; }
        public void setUpdatedMeter(double updatedMeter) { this.updatedMeter = updatedMeter; }
        public String getMillName() { return millName; }
        public void setMillName(String millName) { this.millName = millName; }
        public String getColorCode() { return colorCode; }
        public void setColorCode(String colorCode) { this.colorCode = colorCode; }
    }

    public static class BorderDto {
        private String shopName;
        public String getShopName() { return shopName; }
        public void setShopName(String shopName) { this.shopName = shopName; }
    }
}
