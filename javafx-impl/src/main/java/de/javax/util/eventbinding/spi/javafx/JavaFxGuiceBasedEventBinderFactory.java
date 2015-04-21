package de.javax.util.eventbinding.spi.javafx;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;

import de.javax.util.eventbinding.EventBinder;
import de.javax.util.eventbinding.impl.DefaultEventBinder;
import de.javax.util.eventbinding.source.EventBindingConnectorFactory;
import de.javax.util.eventbinding.source.EventSourceFactory;
import de.javax.util.eventbinding.spi.EventBinderFactory;
import de.javax.util.eventbinding.spi.EventBindingFactory;
import de.javax.util.eventbinding.spi.EventSourceCollector;
import de.javax.util.eventbinding.spi.EventTargetCollector;
import de.javax.util.eventbinding.spi.impl.DefaultEventBindingFactory;
import de.javax.util.eventbinding.spi.impl.source.DefaultEventBindingConnectorFactory;
import de.javax.util.eventbinding.spi.impl.source.DefaultEventSourceFactory;
import de.javax.util.eventbinding.spi.impl.target.DefaultEventTargetCollector;
import de.javax.util.eventbinding.spi.impl.target.MethodEventTargetFactory;
import de.javax.util.eventbinding.spi.impl.target.metadata.DefaultTargetProviderClassAnalyzer;
import de.javax.util.eventbinding.spi.impl.target.metadata.HandlerMethodDescriptorCollector;
import de.javax.util.eventbinding.spi.impl.target.metadata.TargetProviderClassAnalyzer;
import de.javax.util.eventbinding.spi.javafx.source.JavaFxEventSourceCollector;
import de.javax.util.eventbinding.spi.javafx.target.JfxMethodEventTargetFactory;
import de.javax.util.eventbinding.spi.javafx.target.metadata.JfxMethodHandlerDescriptorCollector;

/**
 * An implementation of the event binder factory which is based on Guice.
 *
 * @author Frank Hardy
 */
public class JavaFxGuiceBasedEventBinderFactory implements EventBinderFactory {
	
	@Override
	public EventBinder createEventBinder() {
		return Guice.createInjector(new Module()).getInstance(EventBinder.class);
	}
	
	static class Module extends AbstractModule {
		
		@Override
		protected void configure() {
			bind(EventBinder.class).to(DefaultEventBinder.class);
			bind(EventSourceCollector.class).to(JavaFxEventSourceCollector.class);
			bind(EventTargetCollector.class).to(DefaultEventTargetCollector.class);
			bind(EventBindingFactory.class).to(DefaultEventBindingFactory.class);
			bind(TargetProviderClassAnalyzer.class).to(DefaultTargetProviderClassAnalyzer.class);
			bind(MethodEventTargetFactory.class).to(JfxMethodEventTargetFactory.class);
			bind(HandlerMethodDescriptorCollector.class).to(JfxMethodHandlerDescriptorCollector.class);
			bind(EventSourceFactory.class).to(DefaultEventSourceFactory.class);
			bind(EventBindingConnectorFactory.class).to(DefaultEventBindingConnectorFactory.class);
		}
	}
}
