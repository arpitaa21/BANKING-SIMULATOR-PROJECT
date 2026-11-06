package com.infosys.bankingApplication.repositories;

import com.infosys.bankingApplication.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
