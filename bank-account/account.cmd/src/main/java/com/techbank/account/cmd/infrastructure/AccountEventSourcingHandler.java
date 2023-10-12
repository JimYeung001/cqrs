package com.techbank.account.cmd.infrastructure;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techbank.account.cmd.domain.AccountAggregate;
import com.techbank.cqrs.core.domain.AggregateRoot;
import com.techbank.cqrs.core.events.BaseEvent;
import com.techbank.cqrs.core.handlers.EventSourcingHandler;
import com.techbank.cqrs.core.infrastructure.EventStore;
import com.techbank.cqrs.core.produicers.EventProducer;

@Service
public class AccountEventSourcingHandler implements EventSourcingHandler<AccountAggregate> {

	@Autowired
	private EventStore eventStore;

	@Autowired
	private EventProducer eventProducer;

	@Override
	public void save(AggregateRoot aggregate) {
		eventStore.saveEvent(aggregate.getId(), aggregate.getUncommittedChanges(), aggregate.getVersion());
		aggregate.markChangesAsCommitted();
	}

	@Override
	public AccountAggregate getById(String id) {
		var aggregate = new AccountAggregate();
		var events = eventStore.getEvents(id);
		if (events != null && !events.isEmpty()) {
			events.stream().forEach(e -> aggregate.replayEvent(e));
			var latestVersion = events.stream().map(e -> e.getVersion()).max(Comparator.naturalOrder());
			aggregate.setVersion(latestVersion.get());
		}
		return aggregate;
	}

	@Override
	public void republishEvents() {
		List<String> aggregateIds = eventStore.getAggregateIds();

		for (String id : aggregateIds) {
			AccountAggregate accountAggregate = getById(id);
			if (null == accountAggregate || !accountAggregate.isActive()) {
				continue;
			}

			List<BaseEvent> events = eventStore.getEvents(id);
			for (BaseEvent event : events) {
				eventProducer.produce(event.getClass().getSimpleName(), event);
			}
		}

	}

}
