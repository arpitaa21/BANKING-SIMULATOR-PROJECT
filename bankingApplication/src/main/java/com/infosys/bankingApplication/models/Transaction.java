package com.infosys.bankingApplication.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
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
    private double amount;
    private String timestamp;
    private String description;
    private double postBalance;

    public Transaction(String type, double amount, String timestamp, String description, double postBalance) {
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
