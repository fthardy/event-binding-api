package de.javax.util.eventbinding.spi.impl.target.metadata;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.Mockito;

import spock.lang.Specification;
import de.javax.util.eventbinding.target.EventTargetProvider;

class DefaultTargetProviderClassAnalyzerSpec extends Specification {

	HandlerMethodDescriptorCollector handlerMethodDescriptorCollectorMock = Mock()
	DefaultTargetProviderClassAnalyzer analyzer = new DefaultTargetProviderClassAnalyzer(this.handlerMethodDescriptorCollectorMock)
	
	def 'Should cache results and analyze only once'() {
		when:
			TargetProviderDescriptor descriptor1 = analyzer.getDescriptorFor(Object)
			TargetProviderDescriptor descriptor2 = analyzer.getDescriptorFor(Object)
			
		then:
			handlerMethodDescriptorCollectorMock.collectHandlerMethodDescriptorsFrom(Object) >> ([] as Set)
			0 * handlerMethodDescriptorCollectorMock.collectHandlerMethodDescriptorsFrom(Object)
			
		expect:
			descriptor1 != null
			descriptor1.handlerMethodDescriptors.isEmpty()
			descriptor1.nestedTargetProviderDescriptors.isEmpty()
			descriptor2.is(descriptor2)
	}
	
	def 'Can analyze target providers which refer to nested providers of the same class'() {
		when:
			TargetProviderDescriptor descriptor = analyzer.getDescriptorFor(TargetProviderWithNestedProviderOfTheSameType)
			
		then:
			handlerMethodDescriptorCollectorMock.collectHandlerMethodDescriptorsFrom(TargetProviderWithNestedProviderOfTheSameType) >> ([] as Set)
			
		expect:
			descriptor != null
			descriptor.handlerMethodDescriptors.isEmpty()
			descriptor.nestedTargetProviderDescriptors.size() == 1
			descriptor.nestedTargetProviderDescriptors.iterator().next().field.name == 'nested'
			descriptor.nestedTargetProviderDescriptors.iterator().next().prefix == 'foo'
	}
}

class TargetProviderWithNestedProviderOfTheSameType {
	
	@EventTargetProvider(from='foo') private TargetProviderWithNestedProviderOfTheSameType nested;
}
