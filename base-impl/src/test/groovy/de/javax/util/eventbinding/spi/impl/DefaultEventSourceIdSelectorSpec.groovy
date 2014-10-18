package de.javax.util.eventbinding.spi.impl

import spock.lang.Specification
import de.javax.util.eventbinding.spi.EventSourceId
import de.javax.util.eventbinding.spi.EventSourceIdSelector

class DefaultEventSourceIdSelectorSpec extends Specification {
        
    def 'check expression splitting'() {
        expect:
        result == DefaultEventSourceIdSelector.splitExpression(expression)
        
        where:
        expression       | result
        "a.b.c"          | ['a', 'b', 'c']
        "a.b.*"          | ['a' , 'b' , '*']   
        'foo.bar2._$_.*' | ['foo' , 'bar2' , '_$_', '*']
    }
    
    def 'check part validation'() {
        expect:
        result == DefaultEventSourceIdSelector.isValidPart(part)
        
        where:
        part | result
        'foo' | true
        'bar9' | true
        '__' | true
        '$' | true
        '666' | false
        '' | false
        ' ' | false
        '*' | false
        'a+' | false
        'Abc' | true
        '(' | false
    }
    
    def 'should match any source identifier when only a wildcard is set'() {
        given:
        EventSourceIdSelector selector = new DefaultEventSourceIdSelector('*')
        
        expect:
        true == selector.matches(sourceId)
        
        where:
        sourceId << [
            new EventSourceId('foo'),
            new EventSourceId(['foo','bar','baz']),
            new EventSourceId(['foo','bar'])
        ]
    }
    
    def 'wildcard at end matches only source identifiers with the given prefix'() {
        given:
        EventSourceIdSelector selector = new DefaultEventSourceIdSelector('foo.bar.*');
        
        expect:
        result == selector.matches(sourceId)
        
        where:
        sourceId | result
        new EventSourceId('foo') | false
        new EventSourceId(['foo','bar']) | false
        new EventSourceId(['foo','bar','bums','dings']) | false
        new EventSourceId(['foo','bar','baz']) | true
        new EventSourceId(['foo','bar','böm']) | true
    }
    
    def 'should match exactly'() {
        given:
        EventSourceIdSelector selector = new DefaultEventSourceIdSelector('foo.bar.baz');
        
        expect:
        result == selector.matches(sourceId)
        
        where:
        sourceId | result
        new EventSourceId('foo') | false
        new EventSourceId(['foo','bar']) | false
        new EventSourceId(['foo','bar','böm']) | false
        new EventSourceId(['foo','bar','baz']) | true
        new EventSourceId(['foo','blam','baz']) | false
        new EventSourceId(['foo','bar','baz','dings']) | false
    }
}
