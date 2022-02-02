package com.fetch.demo.service;

import com.fetch.demo.entity.SpentTransaction;
import com.fetch.demo.entity.Transaction;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class TransactionService {

    private static List<Transaction> transactions;
    private HashMap<String, Integer> payersPoints = new HashMap<>();

    private TransactionService (List<Transaction> transactions) {
        TransactionService.transactions = transactions;
    }

    public Transaction saveTransaction(Transaction transaction) {
        Transaction txn = new Transaction(transaction.getPayer(), transaction.getPoints());
        try {
            if (payersPoints.get(txn.getPayer()) == null) {
                payersPoints.put(txn.getPayer(), Integer.valueOf(txn.getPoints()));
            } else {
                int points = payersPoints.get(txn.getPayer()) + txn.getPoints();
                payersPoints.put(txn.getPayer(), Integer.valueOf(points));
            }
            transactions.add(txn);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return txn;
    }

    public List<SpentTransaction> spendPoints(Transaction pointsToSpend) {
        TransactionService.transactions.sort((txn1, txn2) -> txn1.getDate().compareTo(txn2.getDate()));
        List<Transaction> txnList = new ArrayList<>(TransactionService.transactions); //copy of transactions to avoid ConcurrentModificationException

        List<SpentTransaction> spentTransactions = new ArrayList<>();
        int spent = pointsToSpend.getPoints();
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
                }
            } else {
                break;
            }
        }

        return spentTransactions;
    }

    public HashMap<String, Integer> findPayersPoints() {
        return this.payersPoints;
    }

    public List<Transaction> findTransactions() {
        return TransactionService.transactions;
    }
}