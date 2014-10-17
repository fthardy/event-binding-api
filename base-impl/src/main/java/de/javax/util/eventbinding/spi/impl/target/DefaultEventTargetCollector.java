package de.javax.util.eventbinding.spi.impl.target;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.javax.util.eventbinding.EventBindingException;
import de.javax.util.eventbinding.spi.EventTarget;
import de.javax.util.eventbinding.spi.EventTargetCollector;
import de.javax.util.eventbinding.spi.impl.EventSourceId;
import de.javax.util.eventbinding.spi.impl.EventSourceIdSelector;
import de.javax.util.eventbinding.spi.impl.EventSourceIdSelectorFactory;
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
	    return this.collectEventTargetsFrom(targetProvider, this.idSelectorFactory);
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
	protected Set<EventTarget> collectEventTargetsFrom(
	        Object targetProvider, EventSourceIdSelectorFactory selectorFactory) {
	    
	    Class<? extends Object> targetProviderClass = targetProvider.getClass();
        Set<Method> handlerMethods = this.collectEventHandlerMethods(targetProviderClass);

        @SuppressWarnings("unchecked")
        Set<EventTarget> targets = handlerMethods.isEmpty() ? Collections.EMPTY_SET : new HashSet<EventTarget>();
        for (Method targetMethod : handlerMethods) {
            targets.add(this.targetFactory.createEventTarget(
                    targetProvider, targetMethod, this.getSourceIdPattern(targetMethod, selectorFactory)));
        }
        
        try {
            for (Field field : this.collectNestedTargetProviderFields(targetProviderClass)) {
                EventSourceIdSelectorFactory cascadedSelectorFactory = new CascadedSelectorFactory(
                        selectorFactory, selectorFactory.createEventSourceIdSelector(
                                field.getAnnotation(EventTargetProvider.class).from()));
                
                Object nestedTargetProvider = field.get(targetProvider);
                if (nestedTargetProvider == null) {
                    throw new TargetProviderAccessException(
                            "The value of field '" + field.toGenericString() + "' in class '" + 
                                    targetProviderClass + "' is null!");
                }
                
                // --- recursive call ---
                targets.addAll(this.collectEventTargetsFrom(nestedTargetProvider, cascadedSelectorFactory));
            }
        } catch (Exception e) {
            throw new TargetProviderAccessException(
                    "Failed to access field in class '" + targetProviderClass + "'!", e);
        }
        return targets;
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
	protected Set<Field> collectNestedTargetProviderFields(Class<? extends Object> targetProviderClass) {
        return new HashSet<Field>(new Filter<Field>(Arrays.asList(targetProviderClass.getDeclaredFields())).filter(
                new Predicate<Field>() {
                    @Override
                    public boolean apply(Field field) {
                        return field.getAnnotation(EventTargetProvider.class) != null;
                    }
                }).getElements());
    }

    /**
	 * Collect all event handler methods from the given target provider class.
	 * 
	 * @param targetProviderClass
	 *            the class of the target provider object.
	 * 
	 * @return the set of all event handler methods. If none are found the set is empty.
	 */
    protected Set<Method> collectEventHandlerMethods(Class<?> targetProviderClass) {
        return new HashSet<Method>(new Filter<Method>(Arrays.asList(targetProviderClass.getMethods())).filter(
                new Predicate<Method>() {
                    @Override
                    public boolean apply(Method method) {
                        int modifiers = method.getModifiers();
                        
                        Annotation[] parameterAnnotations = method.getParameterAnnotations()[0];
                        
                        boolean isAccepted = Modifier.isPublic(modifiers)
                                && !Modifier.isAbstract(modifiers)
                                && method.getParameterTypes().length == 1
                                && method.getReturnType() == Void.TYPE
                                && parameterAnnotations.length > 0;
                        if (isAccepted) {
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
     * Get the identifier from the annotation if it was defined.
     * 
     * @param eventHandlerMethod
     *            the event handler method.
     * @param selectorFactory
     *            the factory for creating ID-selectors.
     * 
     * @return the source identifier from the annotation. <code>null</code> if
     *         no source identifier was defined.
     */
	protected EventSourceIdSelector getSourceIdPattern(
	        Method eventHandlerMethod, EventSourceIdSelectorFactory selectorFactory) {
		String selectorExpression = null;
		for (Annotation annotation : eventHandlerMethod.getParameterAnnotations()[0]) {
		    if (annotation instanceof HandleEvent) {
		        selectorExpression = ((HandleEvent) annotation).from();
		        if (selectorExpression.isEmpty()) {
		            selectorExpression = "*";
		        }
		        break;
		    }
		}
		assert selectorExpression != null;
		return selectorFactory.createEventSourceIdSelector(selectorExpression);
	}

    /**
     * Will be thrown by
     * {@link DefaultEventTargetCollector#collectEventTargetsFrom(Object)} when
     * the access to the target provider object is not allowed.
     * 
     * @author Frank Hardy
     */
	public static class TargetProviderAccessException extends EventBindingException {

        private static final long serialVersionUID = 3137888076154365395L;
        
        public TargetProviderAccessException(String message) {
            super(message);
        }

        public TargetProviderAccessException(String message, Throwable cause) {
            super(message, cause);
        }
	}
	
	protected static class CascadedSelectorFactory implements EventSourceIdSelectorFactory {
	    
	    private final EventSourceIdSelector preSelector;
	    private final EventSourceIdSelectorFactory selectorFactory;
	    
	    public CascadedSelectorFactory(EventSourceIdSelectorFactory factory, EventSourceIdSelector preSelector) {
	        this.selectorFactory = factory;
            this.preSelector = preSelector;
        }
	    
	    @Override
	    public EventSourceIdSelector createEventSourceIdSelector(String expression) {
	        return new CascadedEventSourceIdSelector(
	                this.preSelector, this.selectorFactory.createEventSourceIdSelector(expression));
	    }
	}
	
	protected static class CascadedEventSourceIdSelector implements EventSourceIdSelector {
	    
	    private final EventSourceIdSelector preSelector;
	    private final EventSourceIdSelector postSelector;
	    
	    public CascadedEventSourceIdSelector(EventSourceIdSelector pre, EventSourceIdSelector post) {
            this.preSelector = pre;
            this.postSelector = post;
        }
	    
	    @Override
	    public boolean matches(EventSourceId sourceId) {
	        return this.preSelector.matches(sourceId) && this.postSelector.matches(sourceId);
	    }
	}
}
