package util;

import lombok.Getter;
import network.EventType;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.NoSuchElementException;

public class GenericEvent<T> {
	private Dictionary<String, String> attribs;

	@Getter
	private EventType id;
	@Getter
	private T payload;

	public GenericEvent<T> respecifyType(EventType type) {
		this.id = type;
		return this;
	}

	public GenericEvent(EventType t, T payload) {
		this.id = t;
		this.payload = payload;
		this.attribs = new Hashtable<>();
	}

	public void insertNewAttribute(String key, String value) {
		attribs.put(key, value);
	}

	public String getValue(String key) {
		String value = attribs.get(key);
		if(!attribs.isEmpty() && value != null) {
			return value;
		}
		throw new NoSuchElementException("Key '" + key + "' is not set in event");
	}
}
