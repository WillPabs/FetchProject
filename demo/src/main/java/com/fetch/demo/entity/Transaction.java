package com.fetch.demo.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Transaction {

    private static int count = 0;
    private int transactionID;
    private String payer;
    private int points;
    private LocalDateTime date;

    public Transaction(String payer, int points) {
        this.transactionID = count++;
        this.payer = payer;
        this.points = points; 
        this.date = LocalDateTime.now();
    }
    
}
