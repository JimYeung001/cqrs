package com.techbank.cqrs.core.domain;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.techbank.cqrs.core.events.BaseEvent;

public abstract class AggregateRoot {

	protected String id;
	protected int version = -1;

	protected final List<BaseEvent> changes = new ArrayList<>();
	private final Logger logger = Logger.getLogger(AggregateRoot.class.getName());

	public String getId() {
		return this.id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public List<BaseEvent> getUncommittedChanges() {
		return this.changes;
	}

	public void markChangesAsCommitted() {
		this.changes.clear();
	}

	protected void applyChange(BaseEvent event, Boolean isNewEvent) {
		try {
			var method = getClass().getDeclaredMethod("apply", event.getClass());
			method.setAccessible(true);
			method.invoke(this, event);

		} catch (NoSuchMethodException e) {
			logger.log(Level.WARNING, MessageFormat.format("The apply method was not found in the aggregate for {0}",
					event.getClass().getName()));
		} catch (Exception e) {
			logger.log(Level.SEVERE, MessageFormat.format("Error applying event to aggregate", e));
		} finally {
			if (isNewEvent) {
				changes.add(event);
			}
		}
	}
	
	public void raiseEvent(BaseEvent event) {
		applyChange(event, true);
	}
	
	public void replayEvent(BaseEvent event) {
		applyChange(event, false);
	}

}