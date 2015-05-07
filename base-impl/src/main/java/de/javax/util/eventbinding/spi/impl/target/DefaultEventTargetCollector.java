package de.javax.util.eventbinding.spi.impl.target;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

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
@Singleton
public class DefaultEventTargetCollector implements EventTargetCollector {

	private final TargetProviderClassAnalyzer targetProviderClassAnalyzer;
	private final MethodEventTargetFactory eventTargetFactory;
	
	@Inject
	public DefaultEventTargetCollector(TargetProviderClassAnalyzer targetProviderClassAnalyzer, MethodEventTargetFactory eventTargetFactory) {
		this.targetProviderClassAnalyzer = targetProviderClassAnalyzer;
		this.eventTargetFactory = eventTargetFactory;
	}
	
	@Override
	public Set<EventTarget> collectEventTargetsFrom(Object eventTargetProvider) {
		return this.collectEventTargets(eventTargetProvider, "");
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
			TargetProviderDescriptor targetProviderDescriptor = this.targetProviderClassAnalyzer.getDescriptorFor(eventTargetProvider.getClass());
			
			for (HandlerMethodDescriptor descriptor : targetProviderDescriptor.getHandlerMethodDescriptors()) {
				eventTargets.add(this.eventTargetFactory.createMethodEventTarget(eventTargetProvider, idSelectorPrefix, descriptor));
			}
			for (TargetProviderFieldDescriptor descriptor : targetProviderDescriptor.getNestedTargetProviderDescriptors()) {
				eventTargets.addAll(this.collectEventTargets(
						this.getNestedTargetProviderFromField(descriptor.getField(), eventTargetProvider),
						buildIdPrefix(idSelectorPrefix, descriptor.getPrefix())));
			}
		}
		return eventTargets;
	}
	
	private String buildIdPrefix(String parentPrefix, String prefix) {
		if (parentPrefix.isEmpty()) {
			return prefix;
		} else if (prefix.isEmpty()) {
			return parentPrefix;
		} else {
			return parentPrefix + '.' + prefix;
		}
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
