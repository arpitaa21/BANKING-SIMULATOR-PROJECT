package com.infosys.bankingApplication.loggers;

import com.infosys.bankingApplication.models.Account;
import com.infosys.bankingApplication.models.Transaction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class ReportWriter {

    private static final String REPORT_PREFIX = "account_";
    private static final String REPORT_EXTENSION = ".txt";

    public static void writeIndividualReports(Collection<Account> accounts) {
        for (Account account : accounts) {
            String fileName = REPORT_PREFIX + account.getId() + REPORT_EXTENSION;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                writer.write("ACCOUNT DETAILS\n---------------------------\n");
                writer.write(account.toString() + "\n\n");
                writer.write("TRANSACTION HISTORY\n---------------------------\n");
                for (Transaction t : account.getHistory()) {
                    writer.write(t.toString() + "\n");
                }
                writer.write("\nCURRENT BALANCE : " + account.getBalance() + "\n");

                TransactionLogger.log("REPORT GENERATED | ACCOUNT=" + account.getId());
            } catch (IOException e) {
                TransactionLogger.log("ERROR GENERATING REPORT | ACCOUNT=" + account.getId());
            }
        }
    }
}
