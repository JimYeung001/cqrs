package com.techbank.cqrs.core.exception;

public class AggregateNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AggregateNotFoundException(String message) {
		super(message);
	}
}
