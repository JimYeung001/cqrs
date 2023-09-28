package com.techbank.account.cmd.infrastructure;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techbank.account.cmd.domain.AccountAggregate;
import com.techbank.account.cmd.domain.EventStoreRepository;
import com.techbank.cqrs.core.events.BaseEvent;
import com.techbank.cqrs.core.events.EventModel;
import com.techbank.cqrs.core.exception.AggregateNotFoundException;
import com.techbank.cqrs.core.exception.ConcurrencyException;
import com.techbank.cqrs.core.infrastructure.EventStore;
import com.techbank.cqrs.core.produicers.EventProducer;

@Service
public class AccountEventStore implements EventStore {

	@Autowired
	private EventProducer eventProducer;
	
	@Autowired
	private EventStoreRepository eventStoreRepository;

	@Override
	public void saveEvent(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
		var eventStream = eventStoreRepository.findByAggregateIdentifier(aggregateId);
		if (expectedVersion != -1 && eventStream.get(eventStream.size() - 1).getVersion() != expectedVersion) {
			throw new ConcurrencyException();
		}

		var version = expectedVersion;
		for (var event : events) {
			version++;
			event.setVersion(version);

			var eventModel = EventModel.builder().aggregateIdentifier(aggregateId).timeStamp(new Date())
					.aggregateType(AccountAggregate.class.getTypeName()).version(version)
					.eventType(event.getClass().getTypeName()).eventData(event).build();

			var persistedEvent = eventStoreRepository.save(eventModel);

			if (!persistedEvent.getId().isEmpty()) {
				// produce event to Kafka
				this.eventProducer.produce(event.getClass().getSimpleName(), event);
			}
		}
	}

	@Override
	public List<BaseEvent> getEvents(String aggregateId) {
		var eventStream = eventStoreRepository.findByAggregateIdentifier(aggregateId);
		if (null == eventStream || eventStream.isEmpty()) {
			throw new AggregateNotFoundException("Incorrect Account ID provided!");
		}
		return eventStream.stream().map(e -> e.getEventData()).collect(Collectors.toList());
	}

}
