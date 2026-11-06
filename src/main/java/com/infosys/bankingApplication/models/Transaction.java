package com.infosys.bankingApplication.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "bank_transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    public static final String DEPOSIT = "DEPOSIT";
    public static final String WITHDRAW = "WITHDRAW";
    public static final String TRANSFER_IN = "TRANSFER_IN";
    public static final String TRANSFER_OUT = "TRANSFER_OUT";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;
    private String timestamp;
    private String description;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal postBalance;

    public Transaction(String type, BigDecimal amount, String timestamp, String description, BigDecimal postBalance) {
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
        this.description = description;
        this.postBalance = postBalance;
    }

    @Override
    public String toString() {
        return type + " | Amount: " + amount + " | Date: " + timestamp +
                (description != null ? " | " + description : "") +
                " | Balance: " + postBalance;
    }
}
