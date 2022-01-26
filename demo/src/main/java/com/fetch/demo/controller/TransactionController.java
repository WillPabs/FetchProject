package com.fetch.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fetch.demo.entity.SpentTransaction;
import com.fetch.demo.entity.Transaction;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {

    private final List<Transaction> transactions;
    private HashMap<String, Integer> payersPoints = new HashMap<>();
    
    private TransactionController(List<Transaction> transactions) {
        this.transactions = transactions;
    }
    
    @GetMapping("/transactions")
    public List<Transaction> getTransactions() {
        return this.transactions;
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
        return this.payersPoints;
    }

    @PostMapping("/transactions/spend")
    public List<SpentTransaction> spendPoints(@RequestBody Transaction spendPoints) {
        this.transactions.sort((txn1, txn2) -> txn1.getDate().compareTo(txn2.getDate()));
        List<Transaction> txnList = new ArrayList<>(this.transactions); //copy of transactions to avoid ConcurrentModificationException
        
        List<SpentTransaction> spentTransactions = new ArrayList<>();
        int spent = spendPoints.getPoints();
        // loop through transactions, subtract points from payer's points
        for (Transaction txn : txnList) {
            if (spent > 0) {
                if (spent - txn.getPoints() < 0) {
                    spent = txn.getPoints() - spent;
                    spentTransactions.add(new SpentTransaction(txn.getPayer(), spent * -1));
                    this.payersPoints.put(txn.getPayer(), spent);
                    txn.setPoints(spent);
                    spent = 0;
                } else {
                    spent -= txn.getPoints();
                    spentTransactions.add(new SpentTransaction(txn.getPayer(), txn.getPoints() * -1));
                    this.payersPoints.put(txn.getPayer(), this.payersPoints.get(txn.getPayer()) - txn.getPoints());
                    txn.setPoints(0);
                    // this.transactions.remove(txn);
                }
            } else {
                break;
            }
        }

        return spentTransactions;        
    }

    @PostMapping("/transactions")
    public ResponseEntity<?> addTransaction(@RequestBody Transaction newTransaction) {
        Transaction txn = new Transaction(newTransaction.getPayer(), newTransaction.getPoints());
        try {
            if (payersPoints.get(txn.getPayer()) == null) {
                payersPoints.put(txn.getPayer(), Integer.valueOf(txn.getPoints()));
            } else {
                int points = payersPoints.get(txn.getPayer()) + txn.getPoints();
                payersPoints.put(txn.getPayer(), Integer.valueOf(points));
            }
            transactions.add(txn);
        } catch (Exception e) {
            return new ResponseEntity<>("Unsuccessful attempt to add transaction", HttpStatus.NOT_FOUND); 
        }
        return new ResponseEntity<>("Successfully added transaction", HttpStatus.CREATED);
    }
    
}
