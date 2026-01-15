package com.infosys.bankingApplication.loggers;

import java.io.FileWriter;

public class TransactionLogger {
    public static void log(String msg) {
        try (FileWriter fw = new FileWriter("transactions.txt", true)) {
            fw.write(msg + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
