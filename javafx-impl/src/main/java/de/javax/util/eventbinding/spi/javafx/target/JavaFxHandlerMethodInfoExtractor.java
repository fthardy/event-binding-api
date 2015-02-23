package de.javax.util.eventbinding.spi.javafx.target;

import de.javax.util.eventbinding.spi.EventSourceIdSelectorFactory;
import de.javax.util.eventbinding.spi.impl.target.AbstractHandlerMethodInfoExtractor;
import de.javax.util.eventbinding.target.HandleJfxEvent;

/**
 * This extractor implementation extracts handler method infos from {@link HandleJfxEvent} annotations.
 *
 * @author Frank Hardy
 */
public class JavaFxHandlerMethodInfoExtractor extends AbstractHandlerMethodInfoExtractor<HandleJfxEvent> {
	
	/**
	 * Creates a new instance of this class.
	 * 
	 * @param idSelectorFactory
	 *            the ID selector factory.
	 */
	public JavaFxHandlerMethodInfoExtractor(EventSourceIdSelectorFactory idSelectorFactory) {
		super(HandleJfxEvent.class, idSelectorFactory);
	}

	@Override
	protected String getIdSelectorExpressionFromAnnotation(HandleJfxEvent annotation) {
		return annotation.from().trim();
	}
	
	@Override
	protected Object getMetaDataFromAnnotation(HandleJfxEvent annotation) {
		return annotation.eventType();
	}
}
