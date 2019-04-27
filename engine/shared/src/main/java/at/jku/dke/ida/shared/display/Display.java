package at.jku.dke.ida.shared.display;

import at.jku.dke.ida.shared.ResourceBundleHelper;

import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Base-class for displays with a display message.
 */
public abstract class Display {

    private final String displayMessage;

    /**
     * Instantiates a new instance of class {@linkplain Display}.
     *
     * @param displayMessage The display message.
     */
    protected Display(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    /**
     * Instantiates a new instance of class {@linkplain Display}.
     *
     * @param displayMessageResourceName The resource name in the {@code DisplayMessages}-resource for the message to display.
     * @param locale                     The locale for the resource name.
     */
    protected Display(String displayMessageResourceName, Locale locale) {
        this.displayMessage = ResourceBundleHelper.getResourceString("DisplayMessages", locale, displayMessageResourceName);
    }

    /**
     * Gets display message.
     *
     * @return the display message
     */
    public String getDisplayMessage() {
        return displayMessage;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Display.class.getSimpleName() + "[", "]")
                .add("displayMessage='" + displayMessage + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Display)) return false;
        Display display = (Display) o;
        return Objects.equals(displayMessage, display.displayMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayMessage);
    }
}