package de.javax.util.eventbinding.spi.impl.target.metadata;

import java.util.Set;

/**
 * Encapsulates the meta data of an event target provider.
 *
 * @author Frank Hardy
 */
public class TargetProviderDescriptor {
	
	private final Set<HandlerMethodDescriptor> handlerMethodDescriptors;
	private final Set<TargetProviderFieldDescriptor> nestedTargetProviderDescriptors;
	
	public TargetProviderDescriptor(
			Set<HandlerMethodDescriptor> handlerMethodDescriptors,
			Set<TargetProviderFieldDescriptor> nestedTargetProviderDescriptors) {
		this.handlerMethodDescriptors = handlerMethodDescriptors;
		this.nestedTargetProviderDescriptors = nestedTargetProviderDescriptors;
	}

	public Set<HandlerMethodDescriptor> getHandlerMethodDescriptors() {
		return this.handlerMethodDescriptors;
	}

	public Set<TargetProviderFieldDescriptor> getNestedTargetProviderDescriptors() {
		return this.nestedTargetProviderDescriptors;
	}
}