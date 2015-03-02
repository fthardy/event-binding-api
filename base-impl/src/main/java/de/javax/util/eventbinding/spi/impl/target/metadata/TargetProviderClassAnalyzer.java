package de.javax.util.eventbinding.spi.impl.target.metadata;


/**
 * Analyzes a given target provider class and returns the meta data encapsulated
 * in a {@link TargetProviderDescriptor}.
 *
 * @author Frank Hardy
 */
public interface TargetProviderClassAnalyzer {
	
	/**
	 * Get the descriptor for a given event target provider class.
	 * 
	 * @param targetProviderClass
	 *            the class of an event target provider.
	 * 
	 * @return the descriptor with the meta data of the event target provider.
	 */
	TargetProviderDescriptor getDescriptorFor(Class<?> targetProviderClass);
}