package com.infosys.bankingApplication.controllers;

import com.infosys.bankingApplication.models.Account;
import com.infosys.bankingApplication.services.AccountService;
import com.infosys.bankingApplication.repositories.AccountRepository;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService service;
    private final AccountRepository repository;

    public AccountController(AccountService service, AccountRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @GetMapping
    public java.util.List<Account> list() { return repository.findAll(); }

    @GetMapping("/{id}")
    public Account get(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new java.util.NoSuchElementException("Account not found"));
    }

    @PostMapping
    public Account create(@RequestBody Account acc) {
        return service.createAccount(acc.getName(), acc.getEmail(), acc.getBalance());
    }

    @PostMapping("/{id}/deposit")
    public void deposit(@PathVariable Long id, @RequestParam BigDecimal amount) { service.deposit(id, amount); }

    @PostMapping("/{id}/withdraw")
    public void withdraw(@PathVariable Long id, @RequestParam BigDecimal amount) { service.withdraw(id, amount); }

    @PostMapping("/transfer")
    public void transfer(@RequestParam Long fromId, @RequestParam Long toId, @RequestParam BigDecimal amount) {
        service.transfer(fromId, toId, amount);
    }
}
