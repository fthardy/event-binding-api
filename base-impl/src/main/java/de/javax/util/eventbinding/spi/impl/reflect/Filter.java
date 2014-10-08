package de.javax.util.eventbinding.spi.impl.reflect;

import java.util.HashSet;
import java.util.Set;

/**
 * A Filter is used to create a subset of a set of given elements
 * by calling {@link #filter(Predicate)}. Multiple calls can be
 * chained as the result of the call is a Filter instance again.
 * @author Matthias Hanisch
 *
 * @param <T> The type of elements the filter will be applied to.
 */
public class Filter<T> {
  
  private final Set<T> elements;

  /**
   * Creates an instance of a filter with the given elements.
   * @param elements
   */
  public Filter(Set<T> elements) {
    this.elements = elements;
  }
  
  /**
   * Returns the elements of this filter.
   * @return
   */
  public Set<T> getElements() {
    return this.elements;
  }
  
  /**
   * Creates a new instance of a filter with all elements of
   * this Filter instance which apply the given predicate.
   * @param predicate The predicate to check the elements with.
   * @return 
   */
  public Filter<T> filter(Predicate<T> predicate) {
    Set<T> result = new HashSet<T>();
    for (T element: this.elements) {
      if (predicate.apply(element)) {
        result.add(element);
      }
    }
    return new Filter<T>(result);
  }
}