package de.javax.util.eventbinding.vaadin;

import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;

import de.javax.util.eventbinding.spi.EventDispatcher;

/**
 * @author Frank Hardy
 */
public class TextChangeListenerAdapter extends AbstractEventDispatcherAdapter implements TextChangeListener {

    private static final long serialVersionUID = 6647961447260329528L;

    /**
     * Creates a new instance of this adapter.
     * 
     * @param dispatcher
     *            the dispatcher to be adapted.
     */
    public TextChangeListenerAdapter(EventDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void textChange(TextChangeEvent event) {
        this.dispatchEvent(event);
    }
}
