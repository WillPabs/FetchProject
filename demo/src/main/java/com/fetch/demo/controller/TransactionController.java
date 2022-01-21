package com.fetch.demo.controller;

import java.util.List;

import com.fetch.demo.entity.Transaction;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/transactions")
    public ResponseEntity<?> addTransaction(@RequestBody Transaction newTransaction) {
        Transaction txn = new Transaction(newTransaction.getPayer(), newTransaction.getPoints());
        transactions.add(txn);
        // fix to return JSON 
        return txn;
    }
    
}
