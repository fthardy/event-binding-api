package de.javax.util.eventbinding.spi;

/**
 * Represents a source of events in an event binding.
 *
 * @author Frank Hardy
 */
public interface EventSource {

	/**
	 * Register a given event dispatcher at the underlying event source.
	 * 
	 * @param eventDispatcher
	 *            the event dispatcher to be registered.
	 */
	void register(EventDispatcher eventDispatcher);
	
	/**
	 * Unregister the registered event dispatcher from the underlying event source.
	 */
	void unregisterEventDispatcher();

	/**
	 * Returns the id of this event source.
	 * @return
	 */
	String getId();
	
	/**
	 * Returns the type of the event source.
	 * @return
	 */
  Class<?> getType();
  
  /**
   * Returns the alias of the event source.
   * @return
   */
  String getAlias();
}
