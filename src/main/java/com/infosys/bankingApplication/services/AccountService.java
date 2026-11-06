package com.infosys.bankingApplication.services;

import com.infosys.bankingApplication.loggers.TransactionLogger;
import com.infosys.bankingApplication.models.Account;
import com.infosys.bankingApplication.repositories.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository repository;
    private final BalanceMonitorService monitorService;

    public AccountService(AccountRepository repository, BalanceMonitorService monitorService) {
        this.repository = repository;
        this.monitorService = monitorService;
    }

    @Transactional
    public Account createAccount(String name, String email, BigDecimal initialDeposit) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Account holder name is required");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email address is required");
        Account account = repository.save(new Account(name, email, initialDeposit));
        TransactionLogger.log("ACCOUNT CREATED | ID=" + account.getId() + " | NAME=" + name + " | BALANCE=" + initialDeposit);
        monitorService.checkBalance(account);
        return account;
    }

    @Transactional
    public void deposit(Long accountId, BigDecimal amount) {
        Account account = repository.findById(accountId).orElseThrow(() -> new java.util.NoSuchElementException("Account not found"));
        account.deposit(amount);
        repository.save(account);
        TransactionLogger.log("DEPOSIT | ACCOUNT=" + accountId + " | AMOUNT=" + amount + " | NEW_BALANCE=" + account.getBalance());
        monitorService.checkBalance(account);
    }

    @Transactional
    public void withdraw(Long accountId, BigDecimal amount) {
        Account account = repository.findById(accountId).orElseThrow(() -> new java.util.NoSuchElementException("Account not found"));
        account.withdraw(amount);
        repository.save(account);
        TransactionLogger.log("WITHDRAW | ACCOUNT=" + accountId + " | AMOUNT=" + amount + " | NEW_BALANCE=" + account.getBalance());
        monitorService.checkBalance(account);
    }

    @Transactional
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        if (fromId == null || toId == null) throw new IllegalArgumentException("Both account IDs are required");
        if (fromId.equals(toId)) throw new IllegalArgumentException("Cannot transfer to the same account");
        Account source = repository.findById(fromId).orElseThrow(() -> new java.util.NoSuchElementException("Source account not found"));
        Account destination = repository.findById(toId).orElseThrow(() -> new java.util.NoSuchElementException("Destination account not found"));
        source.withdraw(amount);
        destination.deposit(amount);
        repository.save(source);
        repository.save(destination);
        monitorService.checkBalance(source);
        monitorService.checkBalance(destination);
        TransactionLogger.log("TRANSFER | FROM=" + fromId + " | TO=" + toId + " | AMOUNT=" + amount);
    }
}
