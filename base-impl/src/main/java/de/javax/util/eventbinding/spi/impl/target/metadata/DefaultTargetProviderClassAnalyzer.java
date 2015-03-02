package de.javax.util.eventbinding.spi.impl.target.metadata;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import de.javax.util.eventbinding.spi.impl.ClassInfoCache;
import de.javax.util.eventbinding.spi.impl.SimpleClassInfoCache;
import de.javax.util.eventbinding.target.EventTargetProvider;

/**
 * The default implementation of a meta data provider.
 *
 * @author Frank Hardy
 */
public class DefaultTargetProviderClassAnalyzer implements TargetProviderClassAnalyzer {

	private final ClassInfoCache<TargetProviderDescriptor> cache;
	private final HandlerMethodDescriptorCollector handlerMethodMetaDataCollector;
	
	public DefaultTargetProviderClassAnalyzer(HandlerMethodDescriptorCollector collectorImpl) {
		this(collectorImpl, new SimpleClassInfoCache<TargetProviderDescriptor>());
	}
	
	public DefaultTargetProviderClassAnalyzer(HandlerMethodDescriptorCollector collectorImpl, ClassInfoCache<TargetProviderDescriptor> cacheImpl) {
		this.handlerMethodMetaDataCollector = collectorImpl;
		this.cache = cacheImpl;
	}
	
	@Override
	public TargetProviderDescriptor getDescriptorFor(Class<?> targetProviderClass) {
		// because of the caching a reference from to nested target provider of the same class is no problem (no recursive calls with stack overflow!)
		if (this.cache.hasNotKey(targetProviderClass)) {
			this.cache.put(
					targetProviderClass,
					new TargetProviderDescriptor(
							this.handlerMethodMetaDataCollector.collectHandlerMethodDescriptorsFrom(targetProviderClass),
							this.collectTargetProviderFieldDescriptors(targetProviderClass)));
		}
		return this.cache.get(targetProviderClass);
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
}
