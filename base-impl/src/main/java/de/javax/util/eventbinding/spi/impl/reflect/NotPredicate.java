package de.javax.util.eventbinding.spi.impl.reflect;

/**
 * A predicate negating a given predicate will apply to.
 * 
 * @author Matthias Hanisch
 *
 * @param <T>
 */
public class NotPredicate<T> implements Predicate<T> {
    private final Predicate<T> predicate;

    public NotPredicate(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean apply(T element) {
        return !this.predicate.apply(element);
    }
}