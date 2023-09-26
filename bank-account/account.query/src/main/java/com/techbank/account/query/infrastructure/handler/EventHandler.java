package com.techbank.account.query.infrastructure.handler;

import com.techbank.account.common.events.AccountClosedEvent;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.FundsDepositedEvent;
import com.techbank.account.common.events.FundsWithdrawnEvent;

public interface EventHandler {

	public void on(AccountOpenedEvent event);

	public void on(FundsDepositedEvent event);

	public void on(FundsWithdrawnEvent event);

	public void on(AccountClosedEvent event);

}
