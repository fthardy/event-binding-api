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
import de.javax.util.eventbinding.spi.impl.EventBindingSpiUtils;
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
    
    /**
     * Creates a new instance of this event target collector.
     * 
     * @param factory
     *            the target factory.
     */
    public DefaultEventTargetCollector(MethodEventTargetFactory factory) {
        if (factory == null) {
            throw new NullPointerException("Undefined method event target factory!");
        }
        this.targetFactory = factory;
    }

	@Override
	public Set<EventTarget> collectEventTargetsFrom(Object targetProvider) {
	    return this.collectEventTargetsFrom(targetProvider, null);
	}

    /**
     * Collect all event targets from a given target provider object.
     * 
     * @param targetProvider
     *            the target provider object.
     * @param sourceIdPrefix
     *            the prefix for the source identifiers.
     * 
     * @return a set with all found event targets. If none are found return an empty set.
     */
	protected Set<EventTarget> collectEventTargetsFrom(Object targetProvider, String sourceIdPrefix) {
        Set<Method> eventHandlerMethods = this.collectEventHandlerMethods(targetProvider.getClass());

        @SuppressWarnings("unchecked")
        Set<EventTarget> allTargets = eventHandlerMethods.isEmpty() ? 
                Collections.EMPTY_SET : new HashSet<EventTarget>();
        for (Method targetMethod : eventHandlerMethods) {
            allTargets.add(this.targetFactory.createEventTarget(
                    targetProvider, targetMethod, this.getSourceId(targetMethod, sourceIdPrefix)));
        }
        
        Set<Field> nestedTargetProviderFields = this.collectNestedTargetProviderFields(targetProvider.getClass());
        if (!nestedTargetProviderFields.isEmpty()) {
            try {
                for (Field field : nestedTargetProviderFields) {
                    String extendedPrefix = EventBindingSpiUtils.extendSourceId(
                            sourceIdPrefix, field.getAnnotation(EventTargetProvider.class).sourceIdPrefix().trim());
                    
                    // Recursive call!
                    allTargets.addAll(this.collectEventTargetsFrom(field.get(targetProvider), extendedPrefix));
                }
            } catch (Exception e) {
                throw new TargetProviderAccessException(e);
            }
        }
        return allTargets;
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
                new EventTargetProviderFieldFilterPredicate()).getElements());
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
                new EventHandlerMethodFilterPredicate()).getElements());
    }

    /**
     * Get the identifier from the annotation if it was defined.
     * 
     * @param eventHandlerMethod
     *            the event handler method.
     * 
     * @return the source identifier from the annotation. <code>null</code> if
     *         no source identifier was defined.
     */
	protected String getSourceId(Method eventHandlerMethod, String sourceIdPrefix) {
		String sourceId = null;
		Annotation[] parameterAnnotations = eventHandlerMethod.getParameterAnnotations()[0];
		
		for (Annotation annotation : parameterAnnotations) {
		    if (annotation instanceof HandleEvent) {
		        sourceId = ((HandleEvent) annotation).fromSource().trim();
		        if (sourceId.isEmpty()) {
		            sourceId = null;
		        }
		    }
		}
		return EventBindingSpiUtils.extendSourceId(sourceIdPrefix, sourceId);
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

        /**
         * Create a new instance of this exception.
         * 
         * @param cause
         *            the causing exception.
         */
        public TargetProviderAccessException(Throwable cause) {
            super("", cause);
        }
	}
	
	/**
	 * The default implementation of the factory for the event targets.
	 * 
	 * @author Frank Hardy
	 */
	public static class DefaultEventTargetFactory implements MethodEventTargetFactory {
	    
	    @Override
	    public EventTarget createEventTarget(Object targetProvider, Method eventHandlerMethod, String sourceId) {
	        return new DefaultEventTarget(
	                sourceId,
	                eventHandlerMethod.getParameterTypes()[0],
	                new MethodAdaptingEventDispatcher(eventHandlerMethod, targetProvider));
	    }
	}

    /**
     * The filter predicate for identifying event handler methods.<br/>
     * Accepts only methods which are public non-abstract and do have one
     * parameter which is annotated with {@link HandleEvent} and have a return
     * type of <code>void</code>.
     * 
     * @author Frank Hardy
     */
	protected static class EventHandlerMethodFilterPredicate implements Predicate<Method> {

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
	}
	
	/**
	 * The filter predicate for identifying event target provider fields.<br/>
	 * Accepts only fields which are annotated with {@link EventTargetProvider}.
	 * 
	 * @author Frank Hardy
	 */
	protected static class EventTargetProviderFieldFilterPredicate implements Predicate<Field> {
	    
	    @Override
	    public boolean apply(Field field) {
	        return field.getAnnotation(EventTargetProvider.class) != null;
	    }
	}
}
