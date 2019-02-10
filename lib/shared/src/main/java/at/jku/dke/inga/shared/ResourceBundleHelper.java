package at.jku.dke.inga.shared;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * A helper-class for retrieving values from resource bundles.
 */
public final class ResourceBundleHelper {

    private static final Logger LOG = LogManager.getLogger(ResourceBundleHelper.class);

    /**
     * Returns the resource string for the resource with the specified name from the given bundle for the current locale.
     *
     * @param bundleName   The name of the bundle.
     * @param resourceName The name of the resource.
     * @return The String or <code>null</code>, if the resource could not be found.
     * @throws NullPointerException if <code>resourceName</code> is <code>null</code>
     */
    public static String getResourceString(String bundleName, String resourceName) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(bundleName);
            return bundle.getString(resourceName);
        } catch (MissingResourceException ex) {
            LOG.fatal("Could not load resource-bundle " + bundleName, ex);
            return null;
        }
    }

    /**
     * Returns the resource string for the resource with the specified name from the given bundle for the specified locale.
     *
     * @param bundleName   The name of the bundle.
     * @param locale       The requested resource.
     * @param resourceName The name of the resource.
     * @return The String or <code>null</code>, if the resource could not be found.
     * @throws NullPointerException if <code>resourceName</code> is <code>null</code>
     */
    public static String getResourceString(String bundleName, Locale locale, String resourceName) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale);
            return bundle.getString(resourceName);
        } catch (MissingResourceException ex) {
            LOG.fatal("Could not load resource-bundle " + bundleName, ex);
            return null;
        }
    }

    /**
     * Prevents generating a instance of this class.
     */
    private ResourceBundleHelper() {
    }
}
