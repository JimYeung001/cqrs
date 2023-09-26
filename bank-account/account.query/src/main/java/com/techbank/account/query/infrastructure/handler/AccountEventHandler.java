package com.techbank.account.query.infrastructure.handler;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techbank.account.common.events.AccountClosedEvent;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.FundsDepositedEvent;
import com.techbank.account.common.events.FundsWithdrawnEvent;
import com.techbank.account.query.domain.AccountRepository;
import com.techbank.account.query.domain.BankAccount;

@Service
public class AccountEventHandler implements EventHandler {

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public void on(AccountOpenedEvent event) {
		BankAccount bankAccount = BankAccount.builder().id(event.getId()).accountHolder(event.getAccountHolder())
				.accountType(event.getAccountType()).balance(event.getOpeningBalance()).build();
		this.accountRepository.save(bankAccount);
	}

	@Override
	public void on(FundsDepositedEvent event) {
		Optional<BankAccount> bankAccount = this.accountRepository.findById(event.getId());
		if (bankAccount.isEmpty()) {
			return;
		}
		double currentBalance = bankAccount.get().getBalance();
		bankAccount.get().setBalance(currentBalance + event.getAmount());
		this.accountRepository.save(bankAccount.get());

	}

	private double getAccountBalance(String id) {
		Optional<BankAccount> bankAccount = this.accountRepository.findById(id);
		if (bankAccount.isEmpty()) {
			return -1;
		}
		return bankAccount.get().getBalance();
	}

	@Override
	public void on(FundsWithdrawnEvent event) {
		Optional<BankAccount> bankAccount = this.accountRepository.findById(event.getId());
		if (bankAccount.isEmpty()) {
			return;
		}
		double currentBalance = bankAccount.get().getBalance();
		bankAccount.get().setBalance(currentBalance - event.getAmount());
		this.accountRepository.save(bankAccount.get());

	}

	@Override
	public void on(AccountClosedEvent event) {
		this.accountRepository.deleteById(event.getId());
	}

}
