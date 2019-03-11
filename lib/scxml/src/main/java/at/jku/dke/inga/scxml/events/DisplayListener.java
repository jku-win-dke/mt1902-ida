package at.jku.dke.inga.scxml.events;

import java.util.EventListener;

/**
 * The listener interface for receiving display data events.
 */
public interface DisplayListener extends EventListener {
    /**
     * Invoked when new display data are available.
     *
     * @param sessionId The session identifier.
     * @param evt       The event to be processed.
     */
    void displayDataAvailable(String sessionId, DisplayEventData evt);
}