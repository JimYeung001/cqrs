package com.techbank.cqrs.core.infrastructure;

import com.techbank.cqrs.core.commonds.BaseCommand;
import com.techbank.cqrs.core.commonds.CommandHandlerMethod;

public interface CommandDispatcher {
	<T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler);

	void send(BaseCommand command);
}
