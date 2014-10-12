package de.javax.util.eventbinding.spi.impl.reflect;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A filter is used to create a subset of a collection of elements by calling
 * {@link #filter(Predicate)}. Multiple calls can be chained as the result of
 * the call is again a filter instance with the new collection of elements.
 * 
 * @author Matthias Hanisch
 * 
 * @param <T>
 *            The type of elements the filter will be applied to.
 */
public class Filter<T> {

    private final Collection<T> elements;

    /**
     * Creates an instance of a filter with the given elements.
     * 
     * @param elements
     *            the elements to be filtered.
     */
    public Filter(Collection<T> elements) {
        this.elements = elements;
    }

    /**
     * Returns the elements of this filter.
     * 
     * @return
     */
    public Collection<T> getElements() {
        return this.elements;
    }

    /**
     * Filter the elements of this filter by a given predicate and return a new
     * filter instance with the new collection of (filtered) elements.
     * 
     * @param predicate
     *            The predicate used to check which elements are accepted by this filter.
     * 
     * @return a new instance of a filter that contains all elements of this
     *         filter which are accepted by the given predicate.
     */
    public Filter<T> filter(Predicate<T> predicate) {
        Collection<T> result = new ArrayList<T>();
        for (T element : this.elements) {
            if (predicate.apply(element)) {
                result.add(element);
            }
        }
        return new Filter<T>(result);
    }
}