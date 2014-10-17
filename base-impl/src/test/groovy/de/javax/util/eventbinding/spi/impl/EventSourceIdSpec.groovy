package de.javax.util.eventbinding.spi.impl

import spock.lang.Specification

class EventSourceIdSpec extends Specification {
    
    def 'disallowed identifier names on instance creation'() {
        expect:
        try {
            new EventSourceId(name)
        } catch (Exception e) {
            e.class == IllegalArgumentException
        }
        
        where:
        name << [null, '', '666', '6abc', 'ab c', 'a.bc', 'ab$c', ' sdkd']
    }
    
    def 'allowed identifier names on instance creation'() {
        expect:'no exception on instance creation'
        new EventSourceId(name) != null
        
        
        where:'the following names are allowed...'
        name << ['abc', 'a1b2c3', '_ab_c_', '$bla']
    }
    
    def 'extention should create new instance'() {
        given:
        EventSourceId id = new EventSourceId('test')
        EventSourceId newId = id.extend('foo')
        
        expect:
        newId != id
        id.names == ['test']
        newId.names == ['test', 'foo']
        id.parentNames == []
        newId.parentNames == ['test']
    }
}
