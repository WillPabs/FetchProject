package com.fetch.demo.controller;

import java.util.HashMap;
import java.util.List;

import com.fetch.demo.entity.SpentTransaction;
import com.fetch.demo.entity.Transaction;

import com.fetch.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/transactions")
    public List<Transaction> getTransactions() {
        return transactionService.findTransactions();
    }

    @GetMapping("/transactions/points")
    public HashMap<String, Integer> getPayersPoints() {
        return transactionService.findPayersPoints();
    }

    @PostMapping("/transactions/spend")
    public List<SpentTransaction> spendPoints(@RequestBody Transaction pointsToSpend) {
        return transactionService.spendPoints(pointsToSpend);
    }

    @PostMapping("/transactions")
    public Transaction addTransaction(@RequestBody Transaction newTransaction) {
        return transactionService.saveTransaction(newTransaction);
    }

}
