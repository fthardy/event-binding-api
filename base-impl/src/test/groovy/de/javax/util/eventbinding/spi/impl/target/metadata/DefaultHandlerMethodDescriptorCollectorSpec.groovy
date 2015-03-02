package de.javax.util.eventbinding.spi.impl.target.metadata

import de.javax.util.eventbinding.target.HandleEvent;
import spock.lang.Specification

class DefaultHandlerMethodDescriptorCollectorSpec extends Specification {

	DefaultHandlerMethodDescriptorCollector collector = new DefaultHandlerMethodDescriptorCollector()
	
	def 'Always returns a set instance'() {
		given:
			Set descriptorSet = collector.collectHandlerMethodDescriptorsFrom(Object)
			
		expect:
			descriptorSet != null
			descriptorSet.isEmpty()
	}
	
	def 'Should collect only public void methods with one annotated parameter'() {
		given:
			Set descriptors = collector.collectHandlerMethodDescriptorsFrom(TargetProviderWithSomeHandlerMethods)
		
		expect:
			descriptors != null
			descriptors.size() == 2
			descriptors*.handlerMethod.name.containsAll(['validHandler1', 'validHandler2'])
			descriptors*.idSelectorExpression.containsAll(['*', 'test.foo.bar'])
	}
}

abstract class TargetProviderWithSomeHandlerMethods {
	
	abstract void abstractHandler(@HandleEvent Object event);
	
	void validHandler1(@HandleEvent Object event) {
		
	}
	
	void validHandler2(@HandleEvent(from='test.foo.bar') Integer event) {
		
	}
	
	void methodWithMoreThanOneParam(@HandleEvent String event, Object someObject) {
		
	}
	
	int methodWithReturnType(@HandleEvent Object event) {
		
	}
	
	void methodWithOutParameter() {
		
	}
	
	private void privateHandler(@HandleEvent Object event) {
		
	}
	
	protected void protectedMethod(@HandleEvent Object event) {
		
	}
}
