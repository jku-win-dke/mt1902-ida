package at.jku.dke.ida.rules.services;

import at.jku.dke.ida.rules.interfaces.ValueIntentServiceModel;
import at.jku.dke.ida.rules.results.EventConfidenceResult;
import org.kie.api.runtime.KieSession;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * This service provides a method that determines the users operation intent.
 *
 * <p>Executes rules belonging to agenda-groups: {@code intent-determination}, {@code value-intent-determination}</p>
 */
public class ValueIntentService extends DroolsService<ValueIntentServiceModel, Collection<EventConfidenceResult>> {

    /**
     * Instantiates a new instance of class {@linkplain ValueIntentService}.
     */
    public ValueIntentService() {
        super(new String[]{"intent-determination", "value-intent-determination"});
    }

    /**
     * Executes the rules using the given model.
     * <p>
     * Inserts the model, the display data and the analysis situation into the Kie session.
     *
     * @param session The new kie session.
     * @param model   The model required by the rules. It is guaranteed, that the model is not {@code null}.
     * @return Result of the query execution
     */
    @Override
    @SuppressWarnings("Duplicates")
    protected Collection<EventConfidenceResult> execute(KieSession session, ValueIntentServiceModel model) {
        logger.info("Determining the value intent");

        // Add data
        session.insert(model);
        session.insert(model.getAnalysisSituation());
        session.insert(model.getDisplayData());

        // Execute rules
        session.fireAllRules();

        // Get results and close session
        Collection<?> result = session.getObjects(obj -> obj instanceof EventConfidenceResult);
        closeSession();

        // Sort and return
        return result.stream()
                .map(x -> (EventConfidenceResult) x)
                .sorted()
                .collect(Collectors.toList());
    }

}
