package com.techbank.account.cmd.api.commands;

import com.techbank.cqrs.core.commonds.BaseCommand;

public interface CommandHandler {
	
	public void handle(BaseCommand command);

}
