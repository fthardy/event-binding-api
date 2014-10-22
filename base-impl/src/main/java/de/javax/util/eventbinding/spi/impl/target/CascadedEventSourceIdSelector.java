package de.javax.util.eventbinding.spi.impl.target;

import de.javax.util.eventbinding.spi.EventSourceId;
import de.javax.util.eventbinding.spi.EventSourceIdSelector;

/**
 * This event source identifier selector implementation allows for cascading two
 * selector instances.<br/>
 * When {@link #matches(EventSourceId)} is called the call is delegated to the
 * two selectors in an exclusive and-expression where the pre-selector is called
 * first. This means that if the pre-selector returns <code>false</code> the
 * post-selector is never been called. Instances of this selector are created by
 * {@link DefaultEventTargetCollector} for each target of a nested target
 * provider. The pre selector represents the expression which has been set at
 * the target provider and the post selector will represent the exression of a
 * target or a further nested target provider.
 * 
 * @author Frank Hardy
 */
class CascadedEventSourceIdSelector implements EventSourceIdSelector {

    private final EventSourceIdSelector preSelector;
    private final EventSourceIdSelector postSelector;

    /**
     * Creates a new instance of this selector.
     * 
     * @param pre
     *            the pre selector.
     * @param post
     *            the post selector.
     */
    public CascadedEventSourceIdSelector(EventSourceIdSelector pre, EventSourceIdSelector post) {
        this.preSelector = pre;
        this.postSelector = post;
    }

    @Override
    public boolean matches(EventSourceId sourceId) {
        return this.preSelector.matches(sourceId) && this.postSelector.matches(sourceId);
    }
}