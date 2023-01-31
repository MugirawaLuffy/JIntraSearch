package util;

import network.EventType;

@FunctionalInterface
public interface IsResponsible {
	public boolean matchId(EventType id);
}
