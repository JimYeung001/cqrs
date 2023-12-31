package com.techbank.account.cmd.domain;

import java.util.Date;

import com.techbank.account.cmd.api.commands.OpenAccountCommand;
import com.techbank.account.common.events.AccountClosedEvent;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.FundsDepositedEvent;
import com.techbank.account.common.events.FundsWithdrawnEvent;
import com.techbank.cqrs.core.domain.AggregateRoot;

public class AccountAggregate extends AggregateRoot {
	private Boolean active;
	private double balance;

	public AccountAggregate() {
	}

	public AccountAggregate(OpenAccountCommand command) {
		raiseEvent(AccountOpenedEvent.builder().id(command.getId()).accountHolder(command.getAccountHolder())
				.createdDate(new Date()).accountType(command.getAccountType()).openingBalance(command.getOpenBalance())
				.build());
	}

	public void apply(AccountOpenedEvent event) {
		this.id = event.getId();
		this.active = true;
		this.balance = event.getOpeningBalance();
	}

	public void depositFunds(double amount) {
		if (!this.active) {
			throw new IllegalStateException("Funds can not be deposited into a closed account!");
		}

		if (amount < 0) {
			throw new IllegalStateException("The deposite amount must be great than zero!");
		}

		raiseEvent(FundsDepositedEvent.builder().id(this.id).amount(amount).build());
	}

	public void apply(FundsDepositedEvent event) {
		this.id = event.getId();
		this.balance += event.getAmount();
	}

	public void withdrawFunds(double amount) {
		if (!this.active) {
			throw new IllegalStateException("Fudns can not be withdrawn from a closed account!");
		}

		raiseEvent(FundsWithdrawnEvent.builder().id(this.id).amount(amount).build());
	}

	public void apply(FundsWithdrawnEvent event) {
		this.id = event.getId();
		this.balance -= event.getAmount();
	}

	public void closeAccount() {
		if (!this.active) {
			throw new IllegalStateException("The bank account is already closed !");
		}

		raiseEvent(AccountClosedEvent.builder().id(this.id).build());
	}

	public void apply(AccountClosedEvent event) {
		this.id = event.getId();
		this.active = false;
	}

	public double getBalance() {
		return this.balance;
	}

	public boolean isActive() {
		return this.active.booleanValue();
	}

}
