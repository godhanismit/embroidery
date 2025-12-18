package com.example.demo.model;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chalans")
@CompoundIndex(name = "user_chalanNo_idx", def = "{'user': 1, 'chalanNo': 1}", unique = true)
public class Chalan {
    @Id
    private String id;
    private String chalanNo;
    private String clothType;
    private List<Bunch> bunches;
    private LocalDate date;
    private double rate;
    private double meter;           // initial meter
    private double updatedMeter;    // updated when processed
    private String millName;
    private String colorCode;
    private String shopName;        // used in border cutting
    private Status status;
    private User user;

    public List<Bunch> getBunches() {
        return bunches;
    }

    public void setBunches(List<Bunch> bunches) {
        this.bunches = bunches;
    }

    public String getClothType() {
        return clothType;
    }

    public void setClothType(String clothType) {
        this.clothType = clothType;
    }

    public Chalan() {
        this.id = UUID.randomUUID().toString();
        this.status = Status.CREATED;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChalanNo() {
        return chalanNo;
    }

    public void setChalanNo(String chalanNo) {
        this.chalanNo = chalanNo;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getMeter() {
        return meter;
    }

    public void setMeter(double meter) {
        this.meter = meter;
    }

    public double getUpdatedMeter() {
        return updatedMeter;
    }

    public void setUpdatedMeter(double updatedMeter) {
        this.updatedMeter = updatedMeter;
    }

    public String getMillName() {
        return millName;
    }

    public void setMillName(String millName) {
        this.millName = millName;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
