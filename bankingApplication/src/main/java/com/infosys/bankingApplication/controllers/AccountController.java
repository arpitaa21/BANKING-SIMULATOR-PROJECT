package com.infosys.bankingApplication.controllers;

import com.infosys.bankingApplication.models.Account;
import com.infosys.bankingApplication.services.AccountService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) { this.service = service; }

    @PostMapping
    public Account create(@RequestBody Account acc) {
        return service.createAccount(acc.getName(), acc.getEmail(), acc.getBalance());
    }

    @PostMapping("/{id}/deposit")
    public void deposit(@PathVariable Long id, @RequestParam double amount) { service.deposit(id, amount); }

    @PostMapping("/{id}/withdraw")
    public void withdraw(@PathVariable Long id, @RequestParam double amount) { service.withdraw(id, amount); }
}
