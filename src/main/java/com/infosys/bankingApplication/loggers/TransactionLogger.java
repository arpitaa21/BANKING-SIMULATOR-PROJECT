package com.infosys.bankingApplication.loggers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionLogger {
    private static final Logger log = LoggerFactory.getLogger(TransactionLogger.class);

    public static void log(String msg) {
        String configuredPath = System.getProperty("banking.transaction-log-path",
                System.getenv().getOrDefault("BANKING_TRANSACTION_LOG_PATH", "transactions.txt"));
        try {
            Path path = Path.of(configuredPath);
            if (path.getParent() != null) Files.createDirectories(path.getParent());
            Files.writeString(path, msg + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException | RuntimeException e) {
            log.warn("Could not append to transaction log", e);
        }
    }
}
