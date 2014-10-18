package de.javax.util.eventbinding.vaadin;

import com.vaadin.ui.Component.Event;
import com.vaadin.ui.Component.Listener;

import de.javax.util.eventbinding.spi.EventDispatcher;

/**
 * A generic event dispatcher adapter for Vaadin component events.<br/>
 * Can be used for all events which are a subtype of type
 * {@link com.vaadin.ui.Component.Event}.
 * 
 * @author Frank Hardy
 */
public class ComponentListenerAdapter extends AbstractEventDispatcherAdapter implements Listener {

    private static final long serialVersionUID = 3678215089589738065L;

    private final Class<Event> eventType;

    /**
     * Create a new instance of this adapter.
     * 
     * @param dispatcher
     *            the event dispatcher to be adapted.
     * @param eventType
     *            the dedicated event type.
     */
    public ComponentListenerAdapter(EventDispatcher dispatcher, Class<Event> eventType) {
        super(dispatcher);

        if (eventType == null) {
            throw new NullPointerException("Undefined event type!");
        }
        this.eventType = eventType;
    }

    @Override
    public void componentEvent(Event event) {
        if (this.eventType.isInstance(event)) {
            this.eventDispatcher.dispatchEvent(event);
        }
    }
}
