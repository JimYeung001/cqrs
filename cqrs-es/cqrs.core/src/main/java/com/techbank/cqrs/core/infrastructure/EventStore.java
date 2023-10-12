package com.techbank.cqrs.core.infrastructure;

import java.util.List;

import com.techbank.cqrs.core.events.BaseEvent;

public interface EventStore {
	
	public void saveEvent(String aggregateId, Iterable<BaseEvent> events, int expectedVersion);
	
	List<BaseEvent> getEvents(String aggregateId);

	public List<String> getAggregateIds();

}
