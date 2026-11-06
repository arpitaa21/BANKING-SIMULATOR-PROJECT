package com.infosys.bankingApplication.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Account {

    public static final BigDecimal MIN_BALANCE = BigDecimal.ZERO;
    public static final BigDecimal MAX_BALANCE = new BigDecimal("1000000.00");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;
    private boolean lowBalanceAlertSent;
    private String createdAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Transaction> history = new ArrayList<>();

    public Account(String name, String email, BigDecimal initialDeposit) {
        initialDeposit = toCurrencyAmount(initialDeposit, "Initial deposit");
        if (initialDeposit.compareTo(MIN_BALANCE) < 0)
            throw new IllegalArgumentException("Initial deposit must be zero or greater");
        if (initialDeposit.compareTo(MAX_BALANCE) > 0)
            throw new IllegalArgumentException("Initial deposit exceeds maximum allowed balance");

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

    public synchronized void deposit(BigDecimal amount) {
        amount = toCurrencyAmount(amount, "Deposit amount");
        if (amount.signum() <= 0)
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        if (balance.add(amount).compareTo(MAX_BALANCE) > 0)
            throw new IllegalArgumentException("Deposit exceeds maximum allowed balance");

        balance = balance.add(amount);
        history.add(new Transaction(Transaction.DEPOSIT, amount, new Date().toString(), null, balance));
    }

    public synchronized void withdraw(BigDecimal amount) {
        amount = toCurrencyAmount(amount, "Withdrawal amount");
        if (amount.signum() <= 0)
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        if (balance.compareTo(amount) < 0)
            throw new com.infosys.bankingApplication.exceptions.InsufficientFundsException("Insufficient balance");

        balance = balance.subtract(amount);
        history.add(new Transaction(Transaction.WITHDRAW, amount, new Date().toString(), null, balance));
    }

    public synchronized void addTransaction(Transaction t) {
        history.add(t);
    }

    private static BigDecimal toCurrencyAmount(BigDecimal value, String fieldName) {
        if (value == null) throw new IllegalArgumentException(fieldName + " is required");
        try {
            return value.setScale(2, RoundingMode.UNNECESSARY);
        } catch (ArithmeticException exception) {
            throw new IllegalArgumentException(fieldName + " must have no more than two decimal places", exception);
        }
    }
}
