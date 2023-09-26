package com.techbank.account.query.infrastructure.consumers;

import org.springframework.kafka.support.Acknowledgment;

import com.techbank.account.common.events.AccountClosedEvent;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.FundsDepositedEvent;
import com.techbank.account.common.events.FundsWithdrawnEvent;

public interface EventConsumer {

	public void consume(AccountOpenedEvent event, Acknowledgment act);

	public void consume(FundsDepositedEvent event, Acknowledgment act);

	public void consume(FundsWithdrawnEvent event, Acknowledgment act);

	public void consume(AccountClosedEvent event, Acknowledgment act);

}
