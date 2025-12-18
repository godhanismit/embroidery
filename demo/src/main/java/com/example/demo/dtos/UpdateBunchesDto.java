package com.example.demo.dtos;

import com.example.demo.model.Bunch;

import java.util.List;

public class UpdateBunchesDto {
    private double rate;
    private List<Bunch> bunches;

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public List<Bunch> getBunches() {
        return bunches;
    }

    public void setBunches(List<Bunch> bunches) {
        this.bunches = bunches;
    }
}
