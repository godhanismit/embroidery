package com.example.demo.controller;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.example.demo.model.Chalan;
import com.example.demo.model.Status;
import com.example.demo.service.ChalanService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chalans")
@CrossOrigin(origins = "*")
public class ChalanController {
    private final ChalanService service;

    public ChalanController(ChalanService service) {
        this.service = service;
    }

    @PostMapping
    public Chalan create(@RequestBody Chalan chalan) {
        // ensure date parsed or set to today if null
        if (chalan.getDate() == null) {
            chalan.setDate(LocalDate.now());
        }
        return service.create(chalan);
    }

    @GetMapping
    public List<Chalan> getAll() {
        return service.findAll();
    }

    @GetMapping("/status/{status}")
    public List<Chalan> getByStatus(@PathVariable String status) {
        Status s = Status.valueOf(status);
        return service.findByStatus(s);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chalan> getById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Transition: send to mill (from CREATED -> AT_MILL)
    @PostMapping("/{id}/to-mill")
    public ResponseEntity<Chalan> toMill(@PathVariable String id, @RequestBody MillDto dto) {
        return service.findById(id)
                .map(chalan -> {
                    chalan.setUpdatedMeter(dto.getUpdatedMeter());
                    chalan.setMillName(dto.getMillName());
                    chalan.setColorCode(dto.getColorCode());
                    chalan.setStatus(Status.AT_MILL);
                    service.save(chalan);
                    return ResponseEntity.ok(chalan);
                }).orElse(ResponseEntity.notFound().build());
    }

    // Transition: mill -> border cutting (AT_MILL -> AT_BORDER_CUTTING)
    @PostMapping("/{id}/to-border")
    public ResponseEntity<Chalan> toBorder(@PathVariable String id, @RequestBody BorderDto dto) {
        return service.findById(id)
                .map(chalan -> {
                    chalan.setShopName(dto.getShopName());
                    chalan.setStatus(Status.AT_BORDER_CUTTING);
                    service.save(chalan);
                    return ResponseEntity.ok(chalan);
                }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/filter")
    public List<Chalan> filter(@RequestBody Map<String, String> filters) {
        return service.filter(filters);
    }


    // Transition: border -> returned to main shop
    @PostMapping("/{id}/return")
    public ResponseEntity<Chalan> toReturn(@PathVariable String id) {
        return service.findById(id)
                .map(chalan -> {
                    chalan.setStatus(Status.RETURNED_TO_MAIN_SHOP);
                    service.save(chalan);
                    return ResponseEntity.ok(chalan);
                }).orElse(ResponseEntity.notFound().build());
    }

    // PDF download
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable String id) {
        return (ResponseEntity<byte[]>) service.findById(id)
                .map(chalan -> {
                    try {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        Document doc = new Document(PageSize.A4);
                        PdfWriter.getInstance(doc, baos);
                        doc.open();

                        Font h1 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
                        Font normal = FontFactory.getFont(FontFactory.HELVETICA, 12);

                        doc.add(new Paragraph("Chalan", h1));
                        doc.add(new Paragraph(" "));
                        doc.add(new Paragraph("Chalan No: " + chalan.getChalanNo(), normal));
                        doc.add(new Paragraph("Date: " + chalan.getDate().format(DateTimeFormatter.ISO_DATE), normal));
                        doc.add(new Paragraph("Rate: " + chalan.getRate(), normal));
                        doc.add(new Paragraph("Meter: " + chalan.getMeter(), normal));
                        doc.add(new Paragraph("Updated Meter: " + chalan.getUpdatedMeter(), normal));
                        doc.add(new Paragraph("Mill Name: " + safe(chalan.getMillName()), normal));
                        doc.add(new Paragraph("Color Code: " + safe(chalan.getColorCode()), normal));
                        doc.add(new Paragraph("Shop Name: " + safe(chalan.getShopName()), normal));
                        doc.add(new Paragraph("Status: " + chalan.getStatus(), normal));

                        doc.close();

                        byte[] pdfBytes = baos.toByteArray();
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_PDF);
                        headers.setContentDispositionFormData("attachment", "chalan-" + chalan.getChalanNo() + ".pdf");
                        return ResponseEntity.ok().headers(headers).body(pdfBytes);
                    } catch (Exception e) {
                        return ResponseEntity.internalServerError().build();
                    }
                }).orElse(ResponseEntity.notFound().build());
    }

    private String safe(String s) {
        return s == null ? "-" : s;
    }

    // DTO classes for request bodies
    public static class MillDto {
        private double updatedMeter;
        private String millName;
        private String colorCode;
        // getters/setters
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
