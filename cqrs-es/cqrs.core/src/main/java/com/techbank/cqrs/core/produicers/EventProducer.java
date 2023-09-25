package com.techbank.cqrs.core.produicers;

import com.techbank.cqrs.core.events.BaseEvent;

public interface EventProducer {

	void produce(String topic, BaseEvent event);

}
