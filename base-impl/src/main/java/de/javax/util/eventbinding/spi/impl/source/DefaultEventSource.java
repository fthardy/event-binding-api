package de.javax.util.eventbinding.spi.impl.source;

import java.lang.reflect.Field;
import java.util.List;

import de.javax.util.eventbinding.spi.EventDispatcher;
import de.javax.util.eventbinding.spi.EventSource;

/**
 * TODO
 * 
 * @author Matthias Hanisch
 */
public class DefaultEventSource implements EventSource {

	private Object source;
  private List<Field> fields;
  private String id;
  private String alias;
  
	public DefaultEventSource(String id, String alias, Object source, List<Field> fields) {
    this.id = id;
    this.alias = alias;
    this.source = source;
    this.fields = fields;
  }
	
	@Override
	public String getAlias() {
	  return alias;
	}
	
	@Override
	public Class<?> getType() {
	  return fields.get(fields.size()-1).getType();
	}
	
	@Override
	public String getId() {
	  return id;
	}

  /** {@inheritDoc} */
	@Override
	public void register(EventDispatcher eventDispatcher) {
	}

	/** {@inheritDoc} */
	@Override
	public void unregisterEventDispatcher() {
	}
}
