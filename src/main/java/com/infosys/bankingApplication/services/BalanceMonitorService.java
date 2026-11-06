package com.infosys.bankingApplication.services;

import com.infosys.bankingApplication.models.Account;
import com.infosys.bankingApplication.repositories.AccountRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class BalanceMonitorService {

    private static final BigDecimal LOW_BALANCE_THRESHOLD = new BigDecimal("500.00");
    private static final BigDecimal MAX_BALANCE_THRESHOLD = Account.MAX_BALANCE;

    private final EmailService emailService;
    private final AccountRepository accountRepository;

    public BalanceMonitorService(EmailService emailService, AccountRepository accountRepository) {
        this.emailService = emailService;
        this.accountRepository = accountRepository;
    }

    public void checkBalance(Account account) {
        boolean saveNeeded = false;

        if (account.getBalance().compareTo(LOW_BALANCE_THRESHOLD) < 0 && !account.isLowBalanceAlertSent()) {
            emailService.sendLowBalanceAlert(account.getEmail(), account.getName(), account.getBalance());
            account.setLowBalanceAlertSent(true);
            saveNeeded = true;
        }

        if (account.getBalance().compareTo(LOW_BALANCE_THRESHOLD) >= 0 && account.isLowBalanceAlertSent()) {
            account.setLowBalanceAlertSent(false);
            saveNeeded = true;
        }

        if (account.getBalance().compareTo(MAX_BALANCE_THRESHOLD) > 0) {
            emailService.sendMaxBalanceAlert(account.getEmail(), account.getName(), account.getBalance());
            saveNeeded = true;
        }

        if (saveNeeded) {
            accountRepository.save(account);
        }
    }
}
