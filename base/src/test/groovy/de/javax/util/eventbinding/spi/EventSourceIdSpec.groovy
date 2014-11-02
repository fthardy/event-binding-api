package de.javax.util.eventbinding.spi

import de.javax.util.eventbinding.spi.EventSourceId;
import spock.lang.Specification

class EventSourceIdSpec extends Specification {
    
    def 'check disallowed identifier names'() {
        expect:
        try {
            new EventSourceId(name)
        } catch (Exception e) {
            e.class == IllegalArgumentException
        }
        
        where:
        name << [null, '', '666', '6abc', 'ab c', 'a.bc', 'ab$c', ' sdkd']
    }
    
    def 'check allowed identifier names'() {
        given:
        EventSourceId id = new EventSourceId(name)
        
        when:
        def names = id.names

        then:
        names[0] == name
        
        expect:'no exception on instance creation'
        id != null
                
        where:'the following names are allowed...'
        name << ['abc', 'a1b2c3', '_ab_c_', '$bla']
    }
}
