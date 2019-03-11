package at.jku.dke.inga.shared.display;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Displays the contents in a simple list.
 */
public class ListDisplay extends Display {

    private final Collection<? extends Displayable> data;

    /**
     * Instantiates a new instance of class {@linkplain ListDisplay}.
     *
     * @param displayMessage The display message.
     * @param data           The data to display in a list.
     */
    public ListDisplay(String displayMessage, Collection<? extends Displayable> data) {
        super(displayMessage);
        this.data = Collections.unmodifiableCollection(data);
    }

    /**
     * Instantiates a new instance of class {@linkplain ListDisplay}.
     *
     * @param displayMessage The display message.
     * @param data           The data to display in a list.
     */
    public ListDisplay(String displayMessage, Iterable<? extends Displayable> data) {
        super(displayMessage);
        this.data = StreamSupport.stream(data.spliterator(), false).collect(Collectors.toUnmodifiableList());
    }

    /**
     * Instantiates a new instance of class {@linkplain Display}.
     *
     * @param displayMessageResourceName The resource name in the {@code DisplayMessages}-resource for the message to display.
     * @param locale                     The locale for the resource name.
     * @param data                       The data to display in a list.
     */
    public ListDisplay(String displayMessageResourceName, Locale locale, Collection<? extends Displayable> data) {
        super(displayMessageResourceName, locale);
        this.data = data;
    }

    /**
     * Returns the data to display.
     *
     * @return The data that should be displayed in a list.
     */
    public Collection<? extends Displayable> getData() {
        return data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", ListDisplay.class.getSimpleName() + "[", "]")
                .add("displayMessage='" + getDisplayMessage() + "'")
                .add("data=" + data)
                .toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListDisplay)) return false;
        if (!super.equals(o)) return false;
        ListDisplay that = (ListDisplay) o;
        return Objects.equals(data, that.data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), data);
    }
}
