package de.javax.util.eventbinding.spi.impl.target;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import de.javax.util.eventbinding.spi.EventSourceId;
import de.javax.util.eventbinding.spi.EventSourceIdSelector;
import de.javax.util.eventbinding.spi.EventSourceIdSelectorFactory;
import de.javax.util.eventbinding.spi.EventTarget;
import de.javax.util.eventbinding.spi.EventTargetCollector;
import de.javax.util.eventbinding.spi.impl.ClassInfoCache;
import de.javax.util.eventbinding.spi.impl.target.TargetProviderClassInfo.HandlerMethodInfo;
import de.javax.util.eventbinding.spi.impl.target.TargetProviderClassInfo.NestedProviderFieldInfo;
import de.javax.util.eventbinding.target.EventTargetProvider;

/**
 * The default implementation of an event target collector.
 * 
 * @author Frank Hardy
 */
public class DefaultEventTargetCollector implements EventTargetCollector {

	private static final String SEPARATOR_WILDCARD = EventSourceIdSelector.SEPARATOR + EventSourceIdSelector.WILDCARD;

	private final MethodEventTargetFactory targetFactory;
	private final EventSourceIdSelectorFactory idSelectorFactory;
	private final ChainedEventSourceIdSelectorFactory chainedIdSelectorFactory;
	private final HandlerMethodInfoCollector handlerMethodInfoCollector;
	private final ClassInfoCache<TargetProviderClassInfo> cache;

	/**
	 * Creates a new instance of this event target collector.
	 * 
	 * @param targetfactory
	 *            the event target factory;
	 * @param idSelectorFactory
	 *            the identifier selector factory.
	 * @param handlerMethodInfoCollector
	 *            the handler method info collector.
	 * @param cache
	 *            the class info cache.
	 */
	public DefaultEventTargetCollector(
			MethodEventTargetFactory targetfactory,
			EventSourceIdSelectorFactory idSelectorFactory,
			HandlerMethodInfoCollector handlerMethodInfoCollector,
			ClassInfoCache<TargetProviderClassInfo> cache) {
		this(targetfactory, idSelectorFactory, null, handlerMethodInfoCollector, cache);
	}

	DefaultEventTargetCollector(
			MethodEventTargetFactory targetfactory,
			EventSourceIdSelectorFactory idSelectorFactory,
			ChainedEventSourceIdSelectorFactory chainedIdSelectorFactory,
			HandlerMethodInfoCollector handlerMethodInfoCollector,
			ClassInfoCache<TargetProviderClassInfo> cache) {
		if (targetfactory == null) {
			throw new NullPointerException("Undefined method event target factory!");
		}
		this.targetFactory = targetfactory;
		
		if (idSelectorFactory == null) {
			throw new NullPointerException("Undefined event source identifier selector factory!");
		}
		this.idSelectorFactory = idSelectorFactory;
		this.chainedIdSelectorFactory = chainedIdSelectorFactory == null ? 
				new DefaultChainedEventSourceIdSelectorFactory() : chainedIdSelectorFactory;
		
		if (handlerMethodInfoCollector == null) {
			throw new NullPointerException("Undefined handler method info collector!");
		}
		this.handlerMethodInfoCollector = handlerMethodInfoCollector;
		
		if (cache == null) {
			throw new NullPointerException("Undefined class info cache!");
		}
		this.cache = cache;
	}

	@Override
	public Set<EventTarget> collectEventTargetsFrom(Object targetProvider) {
		return this.collectTargetsFrom(targetProvider, null);
	}

	/**
	 * Collect all event targets from a given target provider object and all its nested target provider objects.
	 * 
	 * @param targetProvider
	 *            the target provider object.
	 * @param parentIdSelector
	 *            the parent ID-selector.
	 * 
	 * @return a set with all found event targets. If none are found return an
	 *         empty set.
	 */
	private Set<EventTarget> collectTargetsFrom(Object targetProvider, EventSourceIdSelector parentIdSelector) {
		Class<? extends Object> targetProviderClass = targetProvider.getClass();

		TargetProviderClassInfo targetProviderClassInfo = this.getTargetProviderClassInfoFor(targetProviderClass);

		Set<EventTarget> eventTargets = new HashSet<EventTarget>();
		for (HandlerMethodInfo handlerMethodInfo : targetProviderClassInfo.getHandlerMethodInfos()) {
			EventSourceIdSelector idSelector = handlerMethodInfo.getIdSelector();
			if (parentIdSelector != null) {
				idSelector = this.chainedIdSelectorFactory.createChainedIdSelector(parentIdSelector, idSelector);
			}
			eventTargets.add(this.targetFactory.createEventTarget(targetProvider, handlerMethodInfo.getMethod(), idSelector));
		}

		eventTargets.addAll(this.collectEventTargetsFromNestedTargetProviders(
				targetProvider, parentIdSelector, targetProviderClass, targetProviderClassInfo));
		return eventTargets;
	}

	private Set<EventTarget> collectEventTargetsFromNestedTargetProviders(
			Object targetProvider, EventSourceIdSelector parentIdSelector,
			Class<? extends Object> targetProviderClass, TargetProviderClassInfo targetProviderClassInfo) {
		Set<EventTarget> eventTargets = new HashSet<EventTarget>();
		
		// drill recursively down the target provider structure
		for (NestedProviderFieldInfo nestedProviderFieldInfo : targetProviderClassInfo.getNestedProviderFieldInfos()) {
			Field nestedProviderField = nestedProviderFieldInfo.getField();
			nestedProviderField.setAccessible(true); // TODO Handle SecurityException? Configure this or not?
			Object nestedTargetProvider;
			try {
				try {
					nestedTargetProvider = nestedProviderField.get(targetProvider);
				} catch (Exception e) {
					throw new EventTargetAccessException("Failed to access field in class '" + targetProviderClass + "'!", e);
				}
				if (nestedTargetProvider == null) {
					// TODO Maybe ignore nested target provider fields that are null? Or make it at least configurable.
					throw new EventTargetAccessException("The value of field '"
							+ nestedProviderField.toGenericString()
							+ "' in class '" + targetProviderClass
							+ "' is null!");
				}
			} finally {
				nestedProviderField.setAccessible(false); // TODO Handle SecurityException? Configure this or not?
			}

			EventSourceIdSelector nestedProviderIdSelector = nestedProviderFieldInfo.getIdSelector();
			if (parentIdSelector != null) {
				nestedProviderIdSelector = this.chainedIdSelectorFactory.createChainedIdSelector(parentIdSelector, nestedProviderIdSelector);
			}

			// An indirect, recursive call!
			eventTargets.addAll(this.collectTargetsFrom(nestedTargetProvider, nestedProviderIdSelector));
		}
		
		return eventTargets;
	}

	private TargetProviderClassInfo getTargetProviderClassInfoFor(Class<?> targetProviderClass) {
		if (!this.cache.hasKey(targetProviderClass)) {
			this.cache.put(
					targetProviderClass,
					new TargetProviderClassInfo(
							this.handlerMethodInfoCollector.collectHandlerMethodInfosFrom(targetProviderClass),
							this.collectNestedTargetProviderFieldInfos(targetProviderClass)));
		}
		return this.cache.get(targetProviderClass);
	}
	
	private Set<NestedProviderFieldInfo> collectNestedTargetProviderFieldInfos(Class<?> targetProviderClass) {
		Set<NestedProviderFieldInfo> infos = new HashSet<NestedProviderFieldInfo>();
		for (Field field : targetProviderClass.getDeclaredFields()) {
			EventTargetProvider annotation = field.getAnnotation(EventTargetProvider.class);
			if (annotation != null) {
				String selectorExpression = annotation.from();
				if (selectorExpression.isEmpty()) {
					selectorExpression = EventSourceIdSelector.WILDCARD;
				} else if (!selectorExpression.endsWith(SEPARATOR_WILDCARD)) {
					selectorExpression += SEPARATOR_WILDCARD;
				}
				infos.add(new NestedProviderFieldInfo(field, this.idSelectorFactory.createEventSourceIdSelector(selectorExpression)));
			}
		}
		return infos;
	}

	/**
	 * Creates instances of {@link ChainedEventSourceIdSelector}.<br/>
	 * This interface is only internally used for the purpose of unit testing.
	 * 
	 * @author Frank Hardy
	 */
	static interface ChainedEventSourceIdSelectorFactory {
		
		/**
		 * Creates a new cascaded ID selector.
		 * 
		 * @param first
		 *            the first selector instance.
		 * @param next
		 *            the next selector instance.
		 * 
		 * @return the new cascaded ID selector.
		 */
		EventSourceIdSelector createChainedIdSelector(EventSourceIdSelector first, EventSourceIdSelector next);
	}
	
	/**
	 * Default implementation.<br/>
	 * Simply creates an instance of {@link ChainedEventSourceIdSelector}.
	 *
	 * @author Frank Hardy
	 */
	static class DefaultChainedEventSourceIdSelectorFactory implements ChainedEventSourceIdSelectorFactory {
		
		@Override
		public EventSourceIdSelector createChainedIdSelector(EventSourceIdSelector first, EventSourceIdSelector next) {
			return new ChainedEventSourceIdSelector(first, next);
		}
	}

	private static class ChainedEventSourceIdSelector implements EventSourceIdSelector {

		private final EventSourceIdSelector firstSelector;
		private final EventSourceIdSelector nextSelector;

		/**
		 * Creates a new instance of a chained selector.
		 * 
		 * @param first
		 *            the first selector.
		 * @param next
		 *            the next selector.
		 */
		public ChainedEventSourceIdSelector(EventSourceIdSelector first, EventSourceIdSelector next) {
			this.firstSelector = first;
			this.nextSelector = next;
		}

		@Override
		public boolean matches(EventSourceId sourceId) {
			return this.firstSelector.matches(sourceId) && this.nextSelector.matches(sourceId);
		}
	}
}
