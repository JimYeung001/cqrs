package com.techbank.account.cmd.infrastructure;

import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techbank.account.cmd.domain.AccountAggregate;
import com.techbank.cqrs.core.domain.AggregateRoot;
import com.techbank.cqrs.core.handlers.EventSourcingHandler;
import com.techbank.cqrs.core.infrastructure.EventStore;

@Service
public class AccountEventSourcingHandler implements EventSourcingHandler<AccountAggregate> {

	@Autowired
	private EventStore eventStore;
	
	@Override
	public void save(AggregateRoot aggregate) {
		eventStore.saveEvent(aggregate.getId(), aggregate.getUncommittedChanges(), aggregate.getVersion());
		aggregate.markChangesAsCommitted();
	}

	@Override
	public AccountAggregate getById(String id) {
		var aggregate = new AccountAggregate();
		var events = eventStore.getEvents(id);
		if(events != null && !events.isEmpty()) {
			events.stream().forEach(e -> aggregate.replayEvent(e));
			var latestVersion = events.stream().map(e->e.getVersion()).max(Comparator.naturalOrder());
			aggregate.setVersion(latestVersion.get());
		}
		return aggregate;
	}

	

}
