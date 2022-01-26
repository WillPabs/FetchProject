package com.fetch.demo.entity;

import lombok.Data;

@Data
public class SpentTransaction {
    
    private String payer;
    private int points;

    public SpentTransaction(String payer, int points) {
        this.payer = payer; 
        this.points = points;
    }
}
