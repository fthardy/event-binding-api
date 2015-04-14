package de.javax.util.eventbinding.spi.impl.reflect;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Predicate;

/**
 * A predicate methods with a method name matching a regular expression will apply to.
 * 
 * @author Matthias Hanisch
 */
public class MethodNamePredicate implements Predicate<Method> {
    private final Pattern pattern;

    public MethodNamePredicate(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public boolean apply(Method element) {
        Matcher matcher = pattern.matcher(element.getName());
        return matcher.matches();
    }
}