package com.techbank.account.query.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.techbank.cqrs.core.domain.BaseEntity;

public interface AccountRepository extends CrudRepository<BankAccount, String> {

	public Optional<BankAccount> findByAccountHolder(String accountHolder);

	public List<BaseEntity> findByBalanceGreatThan(double balance);

	public List<BaseEntity> findByBalanceLessThan(double balance);
}
