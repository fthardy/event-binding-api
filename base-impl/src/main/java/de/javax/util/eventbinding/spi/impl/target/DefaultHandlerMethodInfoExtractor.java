package de.javax.util.eventbinding.spi.impl.target;

import de.javax.util.eventbinding.spi.EventSourceIdSelectorFactory;
import de.javax.util.eventbinding.target.HandleEvent;

/**
 * The default implementation of a {@link HandlerMethodInfoExtractor} which
 * expects and evaluates {@link HandleEvent} annotations at the event handler methods.
 *
 * @author Frank Hardy
 */
public class DefaultHandlerMethodInfoExtractor extends AbstractHandlerMethodInfoExtractor<HandleEvent> {
	
	/**
	 * Creates a new instance of this class.
	 * 
	 * @param idSelectorFactory
	 *            the ID selector factory.
	 */
	public DefaultHandlerMethodInfoExtractor(EventSourceIdSelectorFactory idSelectorFactory) {
		super(HandleEvent.class, idSelectorFactory);
	}
	
	@Override
	protected String getIdSelectorExpressionFromAnnotation(HandleEvent annotation) {
		return annotation.from().trim();
	}

	@Override
	protected Object getMetaDataFromAnnotation(HandleEvent annotation) {
		return null;
	}
}