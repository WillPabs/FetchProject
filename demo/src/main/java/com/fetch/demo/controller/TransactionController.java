package com.fetch.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fetch.demo.entity.Transaction;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {

    private final List<Transaction> transactions;
    
    private TransactionController(List<Transaction> transactions) {
        this.transactions = transactions;
    }
    
    @GetMapping("/transactions")
    public List<Transaction> getTransactions() {
        return this.transactions;
    }

    @GetMapping("/transactions/{name}")
    public List<Transaction> getTransactions(@PathVariable("name") String name) {
        try {
            List<Transaction> payerTransactions = new ArrayList<>();

            for (Transaction txn : transactions) {
                if (txn.getPayer().equals(name)) {
                    payerTransactions.add(txn);
                }
            }

            return payerTransactions;
        } catch (Exception e) {
            System.out.println("No Payer with that name exists");
        }
        return null;
    }

    @GetMapping("/transactions/points")
    public HashMap<String, Integer> getPayersPoints() {
        HashMap<String, Integer> payersPoints = new HashMap<>();

        // check if payer exists in hashmap, add to map; else add current txn points to existing points
        for (Transaction txn : transactions) {
            if (payersPoints.get(txn.getPayer()) == null) {
                payersPoints.put(txn.getPayer(), Integer.valueOf(txn.getPoints()));
            } else {
                int points = payersPoints.get(txn.getPayer()) + txn.getPoints();
                payersPoints.put(txn.getPayer(), Integer.valueOf(points));
            }
        }

        return payersPoints;
    }

    @PostMapping("/transactions")
    public ResponseEntity<?> addTransaction(@RequestBody Transaction newTransaction) {
        Transaction txn = new Transaction(newTransaction.getPayer(), newTransaction.getPoints());
        transactions.add(txn);
        return new ResponseEntity<>("Successfully added transaction", HttpStatus.CREATED);
    }
    
}
