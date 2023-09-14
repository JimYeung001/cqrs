package com.techbank.account.cmd.infrastructure;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.techbank.cqrs.core.commonds.BaseCommand;
import com.techbank.cqrs.core.commonds.CommandHandlerMethod;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;

@Service
public class AccountCommandDispatcher implements CommandDispatcher {

	private final Map<Class<? extends BaseCommand>, List<CommandHandlerMethod<? extends BaseCommand>>> routes = new HashMap<>();

	@Override
	public <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler) {
		var handlers = routes.computeIfAbsent(type, c -> new LinkedList<>());
		handlers.add(handler);

	}

	@Override
	public void send(BaseCommand command) {
		var handlers = routes.get(command.getClass());

		if (handlers == null || handlers.size() == 0) {
			throw new RuntimeException("No command handler was registered!");
		}
		if (handlers.size() > 1) {
			throw new RuntimeException("Can not send command to more than one hanlder");
		}
		
		handlers.get(0).handle(command);

	}

}
