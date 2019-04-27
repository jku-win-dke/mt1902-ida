package at.jku.dke.ida.app.ruleset.interception;

import at.jku.dke.ida.app.nlp.drools.StringSimilarityService;
import at.jku.dke.ida.app.nlp.models.StringSimilarityServiceModel;
import at.jku.dke.ida.data.models.Similarity;
import at.jku.dke.ida.rules.models.ValueIntentServiceModel;
import at.jku.dke.ida.scxml.interceptors.DetermineValueInputIntentInterceptor;
import at.jku.dke.ida.shared.EventNames;
import at.jku.dke.ida.shared.display.Displayable;
import at.jku.dke.ida.shared.display.ListDisplay;
import at.jku.dke.ida.shared.operations.Operation;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ValueInputIntentInterceptor implements DetermineValueInputIntentInterceptor {

    /**
     * Use this method to modify the model before executing the rules.
     *
     * @param valueIntentServiceModel The basic model.
     * @return The modified model (may also be a new instance or an instance of a subclass).
     */
    @Override
    public ValueIntentServiceModel modifyModel(ValueIntentServiceModel valueIntentServiceModel) {
        if(!(valueIntentServiceModel.getDisplayData() instanceof ListDisplay)) return valueIntentServiceModel;
        // TODO: make also to work with other types

        Collection<Displayable> possibleValues = ((ListDisplay)valueIntentServiceModel.getDisplayData()).getData().stream().map(x -> (Displayable) x).collect(Collectors.toSet());
        possibleValues.add(new Operation(EventNames.EXIT, valueIntentServiceModel.getLocale(), 1));

        // Build Model
        StringSimilarityServiceModel model = new StringSimilarityServiceModel(
                valueIntentServiceModel.getCurrentState(),
                valueIntentServiceModel.getLocale(),
                valueIntentServiceModel.getAnalysisSituation(),
                valueIntentServiceModel.getOperation(),
                valueIntentServiceModel.getAdditionalData(),
                valueIntentServiceModel.getUserInput(),
                possibleValues
        );

        // Execute
        Set<Similarity<Displayable>> result = new StringSimilarityService().executeRules(model);

        // Return
        return new ValueInputIntentModel(
                valueIntentServiceModel.getCurrentState(),
                valueIntentServiceModel.getLocale(),
                valueIntentServiceModel.getAnalysisSituation(),
                valueIntentServiceModel.getOperation(),
                valueIntentServiceModel.getAdditionalData(),
                valueIntentServiceModel.getUserInput(),
                valueIntentServiceModel.getDisplayData(),
                result
        );
    }

}