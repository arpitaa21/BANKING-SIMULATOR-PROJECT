package com.infosys.bankingApplication.services;

import com.infosys.bankingApplication.exceptions.InsufficientFundsException;
import com.infosys.bankingApplication.loggers.TransactionLogger;
import com.infosys.bankingApplication.models.Account;
import com.infosys.bankingApplication.repositories.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository repository;
    private final BalanceMonitorService monitorService;

    public AccountService(AccountRepository repository, BalanceMonitorService monitorService) {
        this.repository = repository;
        this.monitorService = monitorService;
    }

    public Account createAccount(String name, String email, double initialDeposit) {
        Account account = repository.save(new Account(name, email, initialDeposit));
        TransactionLogger.log("ACCOUNT CREATED | ID=" + account.getId() + " | NAME=" + name + " | BALANCE=" + initialDeposit);
        monitorService.checkBalance(account);
        return account;
    }

    public void deposit(Long accountId, double amount) {
        Account account = repository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        account.deposit(amount);
        repository.save(account);
        TransactionLogger.log("DEPOSIT | ACCOUNT=" + accountId + " | AMOUNT=" + amount + " | NEW_BALANCE=" + account.getBalance());
        monitorService.checkBalance(account);
    }

    public void withdraw(Long accountId, double amount) {
        Account account = repository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        if (account.getBalance() < amount) throw new InsufficientFundsException("Insufficient balance");
        account.withdraw(amount);
        repository.save(account);
        TransactionLogger.log("WITHDRAW | ACCOUNT=" + accountId + " | AMOUNT=" + amount + " | NEW_BALANCE=" + account.getBalance());
        monitorService.checkBalance(account);
    }

    public void transfer(Long fromId, Long toId, double amount) {
        if (fromId.equals(toId)) throw new RuntimeException("Cannot transfer to same account");
        withdraw(fromId, amount);
        deposit(toId, amount);
        TransactionLogger.log("TRANSFER | FROM=" + fromId + " | TO=" + toId + " | AMOUNT=" + amount);
    }
}
