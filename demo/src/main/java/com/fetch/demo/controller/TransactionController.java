package com.fetch.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fetch.demo.entity.SpentTransaction;
import com.fetch.demo.entity.Transaction;

import com.fetch.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {

    private final List<Transaction> transactions;
    private HashMap<String, Integer> payersPoints = new HashMap<>();

    @Autowired
    private TransactionService transactionService;

    private TransactionController(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @GetMapping("/transactions")
    public List<Transaction> getTransactions() {
        return transactionService.findTransactions();
    }

    @GetMapping("/transactions/{name}")
    public List<Transaction> getTransactionsByName(@PathVariable("name") String name) {
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
