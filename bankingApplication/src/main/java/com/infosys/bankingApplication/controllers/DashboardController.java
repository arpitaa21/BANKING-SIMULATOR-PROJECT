package com.infosys.bankingApplication.controllers;

import com.infosys.bankingApplication.models.Account;
import com.infosys.bankingApplication.repositories.AccountRepository;
import com.infosys.bankingApplication.services.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class DashboardController {

    private final AccountService accountService;
    private final AccountRepository repository;

    public DashboardController(AccountService accountService, AccountRepository repository) {
        this.accountService = accountService;
        this.repository = repository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("accounts", repository.findAll());
        return "dashboard";
    }

    @GetMapping("/create-account")
    public String createAccountForm(Model model) {
        model.addAttribute("account", new Account());
        return "create-account";
    }

    @PostMapping("/create-account")
    public String createAccount(@ModelAttribute Account account) {
        accountService.createAccount(account.getName(), account.getEmail(), account.getBalance());
        return "redirect:/dashboard";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam Long id, @RequestParam double amount) {
        accountService.deposit(id, amount);
        return "redirect:/dashboard";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam Long id, @RequestParam double amount) {
        accountService.withdraw(id, amount);
        return "redirect:/dashboard";
    }
}
