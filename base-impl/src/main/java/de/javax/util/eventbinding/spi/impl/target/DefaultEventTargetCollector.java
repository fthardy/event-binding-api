package de.javax.util.eventbinding.spi.impl.target;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
import de.javax.util.eventbinding.target.HandleEvent;

/**
 * The default implementation of an event target collector.
 * 
 * @author Frank Hardy
 */
public class DefaultEventTargetCollector implements EventTargetCollector {

	private static final String SEPARATOR_WILDCARD = EventSourceIdSelector.SEPARATOR
			+ EventSourceIdSelector.WILDCARD;

	private final MethodEventTargetFactory targetFactory;
	private final EventSourceIdSelectorFactory idSelectorFactory;
	private final CascadedEventSourceIdSelectorFactory cascadedIdSelectorFactory;
	private final ClassInfoCache<TargetProviderClassInfo> cache;

	/**
	 * Creates a new instance of this event target collector.
	 * 
	 * @param targetfactory
	 *            the event target factory;
	 * @param idSelectorFactory
	 *            the identifier selector factory.
	 */
	public DefaultEventTargetCollector(MethodEventTargetFactory targetfactory,
			EventSourceIdSelectorFactory idSelectorFactory,
			ClassInfoCache<TargetProviderClassInfo> cache) {
		this(targetfactory, idSelectorFactory,
				new CascadedEventSourceIdSelectorFactory() {
					@Override
					public EventSourceIdSelector createCascadedIdSelector(
							EventSourceIdSelector pre,
							EventSourceIdSelector post) {
						return new CascadedEventSourceIdSelector(pre, post);
					}
				}, cache);
	}

	/**
	 * Creates a new instance of this event target collector.<br/>
	 * This constructor is internally used for the purpose of unit testing.
	 * 
	 * @param targetfactory
	 *            the event target factory.
	 * @param isSelectorFactory
	 *            the identifier selector factory.
	 * @param cascadedIdSelectorFactory
	 *            the cascaded identifier selector factory.
	 */
	DefaultEventTargetCollector(MethodEventTargetFactory targetfactory,
			EventSourceIdSelectorFactory isSelectorFactory,
			CascadedEventSourceIdSelectorFactory cascadedIdSelectorFactory,
			ClassInfoCache<TargetProviderClassInfo> cache) {
		if (targetfactory == null) {
			throw new NullPointerException(
					"Undefined method event target factory!");
		}
		this.targetFactory = targetfactory;
		if (isSelectorFactory == null) {
			throw new NullPointerException(
					"Undefined event source identifier selector factory!");
		}
		this.idSelectorFactory = isSelectorFactory;
		this.cascadedIdSelectorFactory = cascadedIdSelectorFactory;
		if (cache == null) {
			throw new NullPointerException("Undefined cache");
		}
		this.cache = cache;
	}

	@Override
	public Set<EventTarget> collectEventTargetsFrom(Object targetProvider) {
		return this.collectTargetsFrom(targetProvider, null);
	}

	/**
	 * Collect all event targets from a given target provider object.
	 * 
	 * @param targetProvider
	 *            the target provider object.
	 * @param parentIdSelector
	 *            the parent ID-selector.
	 * 
	 * @return a set with all found event targets. If none are found return an
	 *         empty set.
	 */
	private Set<EventTarget> collectTargetsFrom(Object targetProvider,
			EventSourceIdSelector parentIdSelector) {
		Class<? extends Object> targetProviderClass = targetProvider.getClass();

		TargetProviderClassInfo info = this.cache.get(targetProviderClass);
		if (info == null) {
			info = new TargetProviderClassInfo(
					this.collectHandlerMethodInfos(targetProviderClass),
					this.collectionNestedTargetProviderFieldInfos(targetProviderClass));
			cache.put(targetProviderClass, info);
		}

		Set<EventTarget> targets = new HashSet<EventTarget>();
		for (HandlerMethodInfo handlerMethodInfo : info.getHandlerMethods()) {
			EventSourceIdSelector idSelector = handlerMethodInfo
					.getIdSelector();
			EventSourceIdSelector combinedSelector = parentIdSelector == null ? idSelector
					: this.cascadedIdSelectorFactory.createCascadedIdSelector(
							parentIdSelector, idSelector);
			targets.add(this.targetFactory.createEventTarget(targetProvider,
					handlerMethodInfo.getMethod(), combinedSelector));
		}

		try { // drill recursively down the target provider structure
			for (NestedProviderFieldInfo nestedProviderFieldInfo : info
					.getNestedProviderFieldInfos()) {
				Field nestedProviderField = nestedProviderFieldInfo.getField();
				nestedProviderField.setAccessible(true);
				Object nestedTargetProvider;
				try {
					nestedTargetProvider = nestedProviderField
							.get(targetProvider);
					if (nestedTargetProvider == null) {
						throw new EventTargetAccessException(
								"The value of field '"
										+ nestedProviderField.toGenericString()
										+ "' in class '" + targetProviderClass
										+ "' is null!");
					}
				} finally {
					nestedProviderField.setAccessible(false);
				}

				EventSourceIdSelector nestedProviderIdSelector = nestedProviderFieldInfo
						.getIdSelector();
				EventSourceIdSelector newParentIdSelector = parentIdSelector == null ? nestedProviderIdSelector
						: this.cascadedIdSelectorFactory
								.createCascadedIdSelector(parentIdSelector,
										nestedProviderIdSelector);

				// --- this is an indirect, recursive call ---
				targets.addAll(this.collectTargetsFrom(nestedTargetProvider,
						newParentIdSelector));
			}
		} catch (Exception e) {
			throw new EventTargetAccessException(
					"Failed to access field in class '" + targetProviderClass
							+ "'!", e);
		}
		return targets;
	}

	private Set<HandlerMethodInfo> collectHandlerMethodInfos(
			Class<?> targetProviderClass) {
		Set<HandlerMethodInfo> infos = new HashSet<HandlerMethodInfo>();
		for (Method method : targetProviderClass.getMethods()) {
			int modifiers = method.getModifiers();
			boolean isAccepted = Modifier.isPublic(modifiers)
					&& !Modifier.isAbstract(modifiers)
					&& method.getParameterTypes().length == 1
					&& method.getReturnType() == Void.TYPE;
			if (isAccepted) {
				Annotation[] parameterAnnotations = method
						.getParameterAnnotations()[0];
				for (Annotation annotation : parameterAnnotations) {
					if (annotation.annotationType() == HandleEvent.class) {
						String selectorExpression = ((HandleEvent) annotation)
								.from().trim();
						if (selectorExpression.isEmpty()) {
							selectorExpression = EventSourceIdSelector.WILDCARD;
						}
						infos.add(new HandlerMethodInfo(
								method,
								this.idSelectorFactory
										.createEventSourceIdSelector(selectorExpression)));
						break;
					}
				}
			}
		}
		return infos;
	}

	private Set<NestedProviderFieldInfo> collectionNestedTargetProviderFieldInfos(
			Class<?> targetProviderClass) {
		Set<NestedProviderFieldInfo> infos = new HashSet<NestedProviderFieldInfo>();
		for (Field field : targetProviderClass.getDeclaredFields()) {
			EventTargetProvider annotation = field
					.getAnnotation(EventTargetProvider.class);
			if (annotation != null) {
				String selectorExpression = annotation.from();
				if (selectorExpression.isEmpty()) {
					selectorExpression = EventSourceIdSelector.WILDCARD;
				} else if (!selectorExpression.endsWith(SEPARATOR_WILDCARD)) {
					selectorExpression += SEPARATOR_WILDCARD;
				}

				infos.add(new NestedProviderFieldInfo(
						field,
						this.idSelectorFactory
								.createEventSourceIdSelector(selectorExpression)));
			}
		}
		return infos;
	}

	/**
	 * This interface is only internally used for the purpose of unit testing.
	 * 
	 * @author Frank Hardy
	 */
	static interface CascadedEventSourceIdSelectorFactory {
		EventSourceIdSelector createCascadedIdSelector(
				EventSourceIdSelector pre, EventSourceIdSelector post);
	}

	private static class CascadedEventSourceIdSelector implements
			EventSourceIdSelector {

		private final EventSourceIdSelector preSelector;
		private final EventSourceIdSelector postSelector;

		/**
		 * Creates a new instance of this selector.
		 * 
		 * @param pre
		 *            the pre selector.
		 * @param post
		 *            the post selector.
		 */
		public CascadedEventSourceIdSelector(EventSourceIdSelector pre,
				EventSourceIdSelector post) {
			this.preSelector = pre;
			this.postSelector = post;
		}

		@Override
		public boolean matches(EventSourceId sourceId) {
			return this.preSelector.matches(sourceId)
					&& this.postSelector.matches(sourceId);
		}
	}
}
