package at.jku.dke.inga.rules.services;

import at.jku.dke.inga.rules.models.ValueServiceModel;
import at.jku.dke.inga.rules.results.ConfidenceResult;
import org.kie.api.runtime.KieSession;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * This service provides a method that determines the selected value.
 *
 * <p>Executes rules belonging to agenda-group: {@code value-determination}</p>
 */
public class ValueService extends DroolsService<ValueServiceModel, Collection<ConfidenceResult>>  {

    /**
     * Instantiates a new instance of class {@linkplain ValueService}.
     */
    public ValueService() {
        super(new String[]{"value-determination"});
    }

    /**
     * Executes the rules using the given model.
     *
     * @param session The new kie session.
     * @param model   The model required by the rules. It is guaranteed, that the model is not {@code null}.
     * @return Result of the query execution
     */
    @Override
    protected Collection<ConfidenceResult> execute(KieSession session, ValueServiceModel model) {
        logger.info("Determining the value");

        // Add data
        session.insert(model);
        session.insert(model.getAnalysisSituation());
        session.insert(model.getDisplayData());

        // Execute rules
        session.fireAllRules();

        // Get results and close session
        Collection<?> result = session.getObjects(obj -> obj instanceof ConfidenceResult);
        closeSession();

        // Sort and return
        return result.stream()
                .map(x -> (ConfidenceResult) x)
                .sorted()
                .collect(Collectors.toList());
    }

}
