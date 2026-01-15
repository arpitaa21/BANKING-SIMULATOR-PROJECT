package com.infosys.bankingApplication.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Account {

    public static final double MIN_BALANCE = 2000;
    public static final double MAX_BALANCE = 1_000_000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private double balance;
    private boolean lowBalanceAlertSent;
    private String createdAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Transaction> history = new ArrayList<>();

    public Account(String name, String email, double initialDeposit) {
        if (initialDeposit < MIN_BALANCE)
            throw new RuntimeException("Initial deposit must be at least " + MIN_BALANCE);
        if (initialDeposit > MAX_BALANCE)
            throw new RuntimeException("Initial deposit exceeds maximum allowed balance");

        this.name = name;
        this.email = email;
        this.balance = initialDeposit;
        this.lowBalanceAlertSent = false;
        this.createdAt = new Date().toString();

        history.add(new Transaction(
                Transaction.DEPOSIT,
                initialDeposit,
                createdAt,
                "Initial Deposit",
                balance
        ));
    }

    public synchronized void deposit(double amount) {
        if (amount <= 0)
            throw new RuntimeException("Invalid deposit amount");
        if (balance + amount > MAX_BALANCE)
            throw new RuntimeException("Deposit exceeds maximum allowed balance");

        balance += amount;
        history.add(new Transaction(Transaction.DEPOSIT, amount, new Date().toString(), null, balance));
    }

    public synchronized void withdraw(double amount) {
        if (amount <= 0)
            throw new RuntimeException("Invalid withdrawal amount");
        if (balance - amount < MIN_BALANCE)
            throw new RuntimeException("Withdrawal denied. Minimum balance of " + MIN_BALANCE + " must be maintained");

        balance -= amount;
        history.add(new Transaction(Transaction.WITHDRAW, amount, new Date().toString(), null, balance));
    }

    public synchronized void addTransaction(Transaction t) {
        history.add(t);
    }
}
