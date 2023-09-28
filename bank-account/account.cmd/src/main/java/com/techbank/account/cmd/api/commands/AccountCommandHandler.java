package com.techbank.account.cmd.api.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techbank.account.cmd.domain.AccountAggregate;
import com.techbank.cqrs.core.commonds.BaseCommand;
import com.techbank.cqrs.core.handlers.EventSourcingHandler;

@Service
public class AccountCommandHandler implements CommandHandler {

	@Autowired
	private EventSourcingHandler<AccountAggregate> eventSourcingHandler;

	@Override
	public void handle(BaseCommand command) {

		if (command instanceof OpenAccountCommand) {
			handleOpenAccountCommand((OpenAccountCommand) command);
		}

		if (command instanceof DepositFundsCommand) {
			handleDepositFundsCommand((DepositFundsCommand) command);
		}

		if (command instanceof WithdrawFundsCommand) {
			handleWithdrawFundsCommand((WithdrawFundsCommand) command);
		}

		if (command instanceof CloseAccountCommand) {
			handleCloseAccountCommand((CloseAccountCommand) command);
		}

	}

	private void handleCloseAccountCommand(CloseAccountCommand command) {
		AccountAggregate aggregate = eventSourcingHandler.getById(command.getId());
		aggregate.closeAccount();
		eventSourcingHandler.save(aggregate);
	}

	private void handleWithdrawFundsCommand(WithdrawFundsCommand command) {
		AccountAggregate aggregate = eventSourcingHandler.getById(command.getId());
		if (command.getAmount() > aggregate.getBalance()) {
			throw new IllegalStateException("Withdraw declined, insufficient funds!");
		}
		aggregate.withdrawFunds(command.getAmount());
		eventSourcingHandler.save(aggregate);

	}

	private void handleDepositFundsCommand(DepositFundsCommand command) {
		AccountAggregate aggregate = eventSourcingHandler.getById(command.getId());
		aggregate.depositFunds(command.getAmount());
		eventSourcingHandler.save(aggregate);

	}

	private void handleOpenAccountCommand(OpenAccountCommand command) {
		var aggregate = new AccountAggregate(command);
		eventSourcingHandler.save(aggregate);
	}

}
