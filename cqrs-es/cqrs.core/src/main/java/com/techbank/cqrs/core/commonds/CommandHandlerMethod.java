package com.techbank.cqrs.core.commonds;

@FunctionalInterface
public interface CommandHandlerMethod<T extends BaseCommand> {
	public void handle(BaseCommand command);
}
