package de.javax.util.eventbinding.spi.impl.reflect;

/**
 * A Predicate can be used to filter a set of elements
 * using {@link Filter}. 
 * @author Matthias
 *
 * @param <T>
 */
public interface Predicate<T> {
  /**
   * Returns whether the given elements applies to this
   * predicate.
   * @param element The element to check.
   * @return <code>True</code> if the element applies to
   * this predicate <code>false</code> otherwise.
   */
  boolean apply(T element);
}