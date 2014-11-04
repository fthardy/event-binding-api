package de.javax.util.eventbinding;

import java.util.Set;

import de.javax.util.eventbinding.spi.EventTarget;

/**
 * This exception will be thrown by
 * {@link DefaultEventBinder#bind(Object, Object)} when the binder is in strict
 * binding mode and any of the found event targets cannot be bound to an event
 * source.
 * 
 * @author Frank Hardy
 */
public final class UnboundTargetsException extends EventBindingException {

    private static final long serialVersionUID = -4640285848126521743L;

    private final String[] unboundTargetInfos;

    /**
     * Creates a new instance of this exception.
     * 
     * @param unboundTargets
     *            the set of unbound targets.
     */
    public UnboundTargetsException(Set<EventTarget> unboundTargets) {
        this.unboundTargetInfos = this.getInfosFromTargets(unboundTargets);
    }

    /**
     * @return a list of text descriptions for each unbound target.
     */
    public String[] getUnboundTargetInfos() {
        return this.unboundTargetInfos;
    }

    private String[] getInfosFromTargets(Set<EventTarget> unboundTargets) {
        String[] infos = new String[unboundTargets.size()];
        int i = 0;
        for (EventTarget target : unboundTargets) {
            infos[i++] = target.toString();
        }
        return infos;
    }
}