package com.example.demo.model;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chalans")
public class Chalan {
    @Id
    private String id;
    private String chalanNo;
    private LocalDate date;
    private double rate;
    private double meter;           // initial meter
    private double updatedMeter;    // updated when processed
    private String millName;
    private String colorCode;
    private String shopName;        // used in border cutting
    private Status status;

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
}
