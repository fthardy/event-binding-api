package de.javax.util.eventbinding.spi.impl.target;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import de.javax.util.eventbinding.spi.EventTarget;
import de.javax.util.eventbinding.spi.EventTargetCollector;
import de.javax.util.eventbinding.spi.impl.target.metadata.HandlerMethodDescriptor;
import de.javax.util.eventbinding.spi.impl.target.metadata.TargetProviderClassAnalyzer;
import de.javax.util.eventbinding.spi.impl.target.metadata.TargetProviderFieldDescriptor;
import de.javax.util.eventbinding.spi.impl.target.metadata.TargetProviderDescriptor;

/**
 * The default implementation of an event target collector which is based on collecting methods as event targets.
 *
 * @author Frank Hardy
 */
public class DefaultEventTargetCollector implements EventTargetCollector {

	private TargetProviderClassAnalyzer metaDataProvider;
	private MethodEventTargetFactory eventTargetFactory;
	
	public DefaultEventTargetCollector(TargetProviderClassAnalyzer metaDataProvider, MethodEventTargetFactory eventTargetFactory) {
		this.metaDataProvider = metaDataProvider;
		this.eventTargetFactory = eventTargetFactory;
	}
	
	@Override
	public Set<EventTarget> collectEventTargetsFrom(Object eventTargetProvider) {
		return this.collectEventTargets(eventTargetProvider, null);
	}
	
	/**
	 * Collects all event targets from the given event target provider instance.<br/>
	 * {@link #collectEventTargetsFrom(Object)} delegates to this method.
	 * 
	 * @param eventTargetProvider
	 *            the event target provider instance.
	 * @param idSelectorPrefix
	 *            the ID-selector expression prefix from the parent event target
	 *            provider instance, if <code>eventTargetProvider</code> is a
	 *            nested event target provider. Otherwise this parameter is
	 *            <code>null</code> (only when called by
	 *            {@link #collectEventTargetsFrom(Object)}).
	 * 
	 * @return the set of the collected event targets.
	 */
	protected Set<EventTarget> collectEventTargets(Object eventTargetProvider, String idSelectorPrefix) {
		Set<EventTarget> eventTargets = new HashSet<EventTarget>();
		if (eventTargetProvider != null) {
			TargetProviderDescriptor targetProviderDescriptor = this.metaDataProvider.getDescriptorFor(eventTargetProvider.getClass());
			
			for (HandlerMethodDescriptor descriptor : targetProviderDescriptor.getHandlerMethodDescriptors()) {
				eventTargets.add(this.eventTargetFactory.createMethodEventTarget(eventTargetProvider, idSelectorPrefix, descriptor));
			}
			for (TargetProviderFieldDescriptor descriptor : targetProviderDescriptor.getNestedTargetProviderDescriptors()) {
				eventTargets.addAll(this.collectEventTargets(
						this.getNestedTargetProviderFromField(descriptor.getField(), eventTargetProvider),
						idSelectorPrefix + '.' +  descriptor.getPrefix()));
			}
		}
		return eventTargets;
	}

	private Object getNestedTargetProviderFromField(Field field, Object fieldOwner) {
		field.setAccessible(true);
		try {
			return field.get(fieldOwner);
		} catch (Exception e) {
			throw new EventTargetAccessException("Failed to access field '" + field.toGenericString() + "' in class '" + fieldOwner.getClass() + "'!", e);
		} finally {
			field.setAccessible(false);
		}
	}
}
