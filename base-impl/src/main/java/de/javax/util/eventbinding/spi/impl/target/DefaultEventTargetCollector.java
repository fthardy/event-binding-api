package de.javax.util.eventbinding.spi.impl.target;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.javax.util.eventbinding.spi.EventSourceIdSelector;
import de.javax.util.eventbinding.spi.EventSourceIdSelectorFactory;
import de.javax.util.eventbinding.spi.EventTarget;
import de.javax.util.eventbinding.spi.EventTargetCollector;
import de.javax.util.eventbinding.spi.impl.reflect.Filter;
import de.javax.util.eventbinding.spi.impl.reflect.Predicate;
import de.javax.util.eventbinding.target.EventTargetProvider;
import de.javax.util.eventbinding.target.HandleEvent;

/**
 * The default implementation of an event target collector.
 * 
 * @author Frank Hardy
 */
public class DefaultEventTargetCollector implements EventTargetCollector {
    
    private static final String SEPARATOR_WILDCARD = EventSourceIdSelector.SEPARATOR + EventSourceIdSelector.WILDCARD;
    
    private final MethodEventTargetFactory targetFactory;
    private final EventSourceIdSelectorFactory idSelectorFactory;
    
    /**
     * Creates a new instance of this event target collector.
     * 
     * @param targetfactory
     *            the target factory.
     */
    public DefaultEventTargetCollector(
            MethodEventTargetFactory targetfactory, EventSourceIdSelectorFactory selectorFactory) {
        if (targetfactory == null) {
            throw new NullPointerException("Undefined method event target factory!");
        }
        this.targetFactory = targetfactory;
        if (selectorFactory == null) {
            throw new NullPointerException("Undefined event source identifier selector factory!");
        }
        this.idSelectorFactory = selectorFactory;
    }

	@Override
	public Set<EventTarget> collectEventTargetsFrom(Object targetProvider) {
	    return this.collectTargetsFrom(targetProvider, this.idSelectorFactory);
	}

    /**
     * Collect all event targets from a given target provider object.
     * 
     * @param targetProvider
     *            the target provider object.
     * @param selectorFactory
     *            the factory for creating ID-selectors.
     * 
     * @return a set with all found event targets. If none are found return an empty set.
     */
	protected Set<EventTarget> collectTargetsFrom(Object targetProvider, EventSourceIdSelectorFactory selectorFactory) {
	    Class<? extends Object> targetProviderClass = targetProvider.getClass();
	    
	    // Collect event handler methods and create an event target for each one
        Set<Method> handlerMethods = this.collectHandlerMethods(targetProviderClass);
        @SuppressWarnings("unchecked")
        Set<EventTarget> targets = handlerMethods.isEmpty() ? Collections.EMPTY_SET : new HashSet<EventTarget>();
        for (Method targetMethod : handlerMethods) {
            targets.add(this.targetFactory.createEventTarget(
                    targetProvider, targetMethod, this.getSourceIdSelector(targetMethod, selectorFactory)));
        }
        
        try { // drill recursively down the target provider structure
            for (Field field : this.collectNestedTargetProviderFieldsFrom(targetProviderClass)) {
                targets.addAll(this.collectNestedTargetsFromProviderField(
                        field, targetProvider, targetProviderClass, selectorFactory));
            }
        } catch (Exception e) {
            throw new EventTargetAccessException(
                    "Failed to access field in class '" + targetProviderClass + "'!", e);
        }
        return targets;
	}

    /**
     * Collect all event handler methods from the given target provider class.
     * 
     * @param targetProviderClass
     *            the class of the target provider object.
     * 
     * @return the set of all event handler methods. If none are found the set is empty.
     */
    private Set<Method> collectHandlerMethods(Class<?> targetProviderClass) {
        return new HashSet<Method>(new Filter<Method>(Arrays.asList(targetProviderClass.getMethods())).filter(
                new Predicate<Method>() {
                    @Override
                    public boolean apply(Method method) {
                        int modifiers = method.getModifiers();
                        
                        
                        boolean isAccepted = Modifier.isPublic(modifiers)
                                && !Modifier.isAbstract(modifiers)
                                && method.getParameterTypes().length == 1
                                && method.getReturnType() == Void.TYPE;
                        if (isAccepted) {
                            Annotation[] parameterAnnotations = method.getParameterAnnotations()[0];
                            isAccepted = false;
                            for (Annotation annotation : parameterAnnotations) {
                                if (annotation.annotationType() == HandleEvent.class) {
                                    isAccepted = true;
                                    break;
                                }
                            }
                        }
                        return isAccepted;
                    }
                }).getElements());
    }

    /**
     * Collect all nested event target provider fields from the given target
     * provider class.
     * 
     * @param targetProviderClass
     *            the class of the target provider object.
     *            
     * @return the set of fields declaring nested event target providers. If none are found the set is empty.
     */
	private Set<Field> collectNestedTargetProviderFieldsFrom(Class<? extends Object> targetProviderClass) {
        return new HashSet<Field>(new Filter<Field>(Arrays.asList(targetProviderClass.getDeclaredFields())).filter(
                new Predicate<Field>() {
                    @Override
                    public boolean apply(Field field) {
                        return field.getAnnotation(EventTargetProvider.class) != null;
                    }
                }).getElements());
    }

    /**
     * Get the identifier from the annotation if it was defined.
     * 
     * @param handlerMethod
     *            the event handler method.
     * @param selectorFactory
     *            the factory for creating ID-selectors.
     * 
     * @return the source identifier from the annotation. <code>null</code> if
     *         no source identifier was defined.
     */
	private EventSourceIdSelector getSourceIdSelector(
	        Method handlerMethod, EventSourceIdSelectorFactory selectorFactory) {
		String selectorExpression = null;
		for (Annotation annotation : handlerMethod.getParameterAnnotations()[0]) {
		    if (annotation instanceof HandleEvent) {
		        selectorExpression = ((HandleEvent) annotation).from();
		        if (selectorExpression.isEmpty()) {
		            selectorExpression = EventSourceIdSelector.WILDCARD;
		        }
		        break;
		    }
		}
		assert selectorExpression != null;
		return selectorFactory.createEventSourceIdSelector(selectorExpression);
	}

	/**
	 * This method is a part of {@link #collectTargetsFrom(Object, EventSourceIdSelectorFactory)}.
	 */
    private Set<EventTarget> collectNestedTargetsFromProviderField(
            Field field, Object targetProvider, Class<?> targetProviderClass,
            EventSourceIdSelectorFactory selectorFactory) throws IllegalAccessException {
        
        String selectorExpression = field.getAnnotation(EventTargetProvider.class).from();
        if (selectorExpression.isEmpty()) {
            selectorExpression = EventSourceIdSelector.WILDCARD;
        } else if (!selectorExpression.endsWith(SEPARATOR_WILDCARD)) {
            selectorExpression += SEPARATOR_WILDCARD;
        }
        
        EventSourceIdSelectorFactory cascadedSelectorFactory = new CascadedSelectorFactory(
                selectorFactory, selectorFactory.createEventSourceIdSelector(selectorExpression));
        
        field.setAccessible(true);
        Object nestedTargetProvider = field.get(targetProvider);
        if (nestedTargetProvider == null) {
            throw new EventTargetAccessException(
                    "The value of field '" + field.toGenericString() + "' in class '" + 
                            targetProviderClass + "' is null!");
        }
        
        // --- this is an indirect, recursive call ---
        return this.collectTargetsFrom(nestedTargetProvider, cascadedSelectorFactory);
    }
}
