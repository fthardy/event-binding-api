package de.javax.util.eventbinding.spi.impl.target.metadata;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import de.javax.util.eventbinding.target.EventTargetProvider;

/**
 * The default implementation of a meta data provider.
 *
 * @author Frank Hardy
 */
public class DefaultTargetProviderClassAnalyzer implements TargetProviderClassAnalyzer {

	private final Cache<Class<?>, TargetProviderDescriptor> cache;
	private final HandlerMethodDescriptorCollector handlerMethodMetaDataCollector;
		
	public DefaultTargetProviderClassAnalyzer(HandlerMethodDescriptorCollector collectorImpl) {
		this(collectorImpl, CacheBuilder.newBuilder().<Class<?>, TargetProviderDescriptor>build());
	}
	
	public DefaultTargetProviderClassAnalyzer(HandlerMethodDescriptorCollector collectorImpl, Cache<Class<?>, TargetProviderDescriptor> cacheImpl) {
		assert collectorImpl != null : "No collector implementation defined!";
		assert cacheImpl != null : "No cache implementation defined!";
		this.handlerMethodMetaDataCollector = collectorImpl;
		this.cache = cacheImpl;
	}
	
	@Override
	public TargetProviderDescriptor getDescriptorFor(final Class<?> targetProviderClass) {
		// because of the caching a reference from to nested target provider of the same class is no problem (no recursive calls with stack overflow!)
		try {
			return this.cache.get(targetProviderClass, new Callable<TargetProviderDescriptor>() {
				@Override
				public TargetProviderDescriptor call() throws Exception {
					return new TargetProviderDescriptor(
							handlerMethodMetaDataCollector.collectHandlerMethodDescriptorsFrom(targetProviderClass),
							collectTargetProviderFieldDescriptors(targetProviderClass));
				}
			});
		} catch (ExecutionException e) {
			throw new TargetProviderDescriptorCreateException(e);
		}
	}
	
	private Set<TargetProviderFieldDescriptor> collectTargetProviderFieldDescriptors(Class<?> targetProviderClass) {
		Set<TargetProviderFieldDescriptor> descriptors = new HashSet<TargetProviderFieldDescriptor>();
		for (Field field : targetProviderClass.getDeclaredFields()) {
			EventTargetProvider annotation = field.getAnnotation(EventTargetProvider.class);
			if (annotation != null) {
				descriptors.add(new TargetProviderFieldDescriptor(field, annotation.from()));
			}
		}
		return descriptors;
	}
	
	public static class TargetProviderDescriptorCreateException extends RuntimeException {
		
		private static final long serialVersionUID = 6650624968622050228L;

		public TargetProviderDescriptorCreateException(Exception e) {
			super(e);
		}
	}
}
