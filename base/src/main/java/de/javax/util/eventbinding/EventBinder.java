package de.javax.util.eventbinding;


/**
 * An event binder is a utility used to create connections between sources which are sending events
 * and targets which can process those events.<br/>
 * 
 * 
 * @author Frank Hardy
 */
public interface EventBinder {

	/**
	 * Make an event binding between event sources and event targets.
	 * 
	 * @param sourceProvider
	 *            an object which provides event sources.
	 * @param targetProvider
	 *            an object which provides event targets.
	 * 
	 * @return the binding instance which represents the event binding between the event
	 *         sources and targets.
	 * 
	 * @throws EventBindingException
	 *             when the binding fails for some reason.
	 */
	EventBinding bind(Object sourceProvider, Object targetProvider) throws EventBindingException;
	
	/**
	 * @return <code>true</code> if the receiving binder instance is in strict binding mode.
	 */
	boolean isInStrictBindingMode();
	
	/**
	 * @param strictBindingOn set to <code>true</code> to activate or deactivate strict binding mode.
	 */
	void setInStrictBindingMode(boolean strictBindingOn);
}