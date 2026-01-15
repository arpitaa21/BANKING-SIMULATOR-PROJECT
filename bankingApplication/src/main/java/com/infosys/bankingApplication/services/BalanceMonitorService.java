package com.infosys.bankingApplication.services;

import com.infosys.bankingApplication.models.Account;
import com.infosys.bankingApplication.repositories.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class BalanceMonitorService {

    private static final double LOW_BALANCE_THRESHOLD = Account.MIN_BALANCE;
    private static final double MAX_BALANCE_THRESHOLD = Account.MAX_BALANCE;

    private final EmailService emailService;
    private final AccountRepository accountRepository;

    public BalanceMonitorService(EmailService emailService, AccountRepository accountRepository) {
        this.emailService = emailService;
        this.accountRepository = accountRepository;
    }

    public void checkBalance(Account account) {
        boolean saveNeeded = false;

        if (account.getBalance() < LOW_BALANCE_THRESHOLD && !account.isLowBalanceAlertSent()) {
            emailService.sendLowBalanceAlert(account.getEmail(), account.getName(), account.getBalance());
            account.setLowBalanceAlertSent(true);
            saveNeeded = true;
        }

        if (account.getBalance() >= LOW_BALANCE_THRESHOLD && account.isLowBalanceAlertSent()) {
            account.setLowBalanceAlertSent(false);
            saveNeeded = true;
        }

        if (account.getBalance() > MAX_BALANCE_THRESHOLD) {
            emailService.sendMaxBalanceAlert(account.getEmail(), account.getName(), account.getBalance());
            saveNeeded = true;
        }

        if (saveNeeded) {
            accountRepository.save(account);
        }
    }
}
