package de.javax.util.eventbinding.impl;

class EventSourceAndTargetProvider {
	
	final Object sourceProvider;
	final Object targetProvider;
	
	public EventSourceAndTargetProvider(Object sourceProvider, Object targetProvider) {
		if (sourceProvider == null) {
			throw new IllegalArgumentException("Undefined event source provider!");
		}
    	this.sourceProvider = sourceProvider;
		if (targetProvider == null) {
			throw new IllegalArgumentException("Undefined event target provider!");
		}
    	this.targetProvider = targetProvider;
	}
}