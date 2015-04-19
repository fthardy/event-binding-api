package de.javax.util.eventbinding;

import java.util.Set;

import de.javax.util.eventbinding.spi.EventTarget;

/**
 * This exception will be thrown by an {@link EventBinder} when it is in strict binding mode and any of the found event
 * targets cannot be bound to any event source.
 * 
 * @author Frank Hardy
 */
public final class UnboundEventTargetsException extends EventBindingException {
	
	private static final long serialVersionUID = 1L;
	
	private static String createMessage(Set<EventTarget> unboundTargets) {
		StringBuilder messageBuilder = new StringBuilder(String.format(
				"%d targets could not be bound to a source:", unboundTargets.size()));
		for (EventTarget target : unboundTargets) {
			messageBuilder.append("\n   ").append(target.getDescription());
		}
		return messageBuilder.toString();
	}
	
	private final String[] targetDescriptions;

	/**
	 * Creates a new instance of this exception.
	 * 
	 * @param unboundTargets
	 *            the set of unbound targets.
	 */
	public UnboundEventTargetsException(Set<EventTarget> unboundTargets) {
		super(createMessage(unboundTargets));
		this.targetDescriptions = this.getDescriptionsFrom(unboundTargets);
	}

	/**
	 * @return a list of text descriptions of the unbound targets.
	 */
	public String[] getTargetDescriptions() {
		return this.targetDescriptions;
	}
	
	private String[] getDescriptionsFrom(Set<EventTarget> unboundTargets) {
		String[] descriptions = new String[unboundTargets.size()];
		int i = 0;
		for (EventTarget target : unboundTargets) {
			descriptions[i++] = target.getDescription();
		}
		return descriptions;
	}
}