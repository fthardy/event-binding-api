package de.javax.util.eventbinding.spi;

import java.util.Set;

/**
 * Represents a target for events in a event binding.
 * 
 * @author Frank Hardy
 * 
 * @see EventTargetCollector
 */
public interface EventTarget {
    
    /**
     * TODO
     * @return
     */
    Class<?> getEventType();
    
    /**
     * TODO
     * @return
     */
    EventSourceIdSelector getEventSourceIdSelector();
    
    /**
     * TODO
     * @return
     */
    EventDispatcher getEventDispatcher();
    
    /**
     * TODO
     * @param boundSources
     */
    void setBoundSources(Set<EventSource> boundSources);
    
	/**
	 * Release all source bindings of the receiving event target.
	 */
	void release();
}
