package at.jku.dke.inga.web.models;

import at.jku.dke.inga.shared.display.Display;
import at.jku.dke.inga.shared.display.ErrorDisplay;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisplayResultTest {

    @Test
    void testConstructorWithGetter() {
        // Prepare
        Display display = new ErrorDisplay("ABC");
        DisplayResult displayResult = new DisplayResult(display);

        // Execute
        String type = displayResult.getType();

        // Assert
        assertEquals(display.getClass().getSimpleName(), type);
        assertEquals(display, displayResult.getDisplay());
    }

}
