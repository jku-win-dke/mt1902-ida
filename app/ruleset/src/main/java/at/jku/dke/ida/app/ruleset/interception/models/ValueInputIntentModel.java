package at.jku.dke.ida.app.ruleset.interception.models;

import at.jku.dke.ida.data.models.Similarity;
import at.jku.dke.ida.rules.models.DefaultValueIntentServiceModel;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.display.Display;
import at.jku.dke.ida.shared.display.Displayable;
import at.jku.dke.ida.shared.models.EngineAnalysisSituation;
import at.jku.dke.ida.shared.session.SessionModel;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

/**
 * Used by the {@link at.jku.dke.ida.app.ruleset.interception.interceptors.ValueInputIntentInterceptor}.
 */
public class ValueInputIntentModel extends DefaultValueIntentServiceModel {

    private final Collection<Similarity<Displayable>> similarities;

    /**
     * Instantiates a new instance of class {@link DefaultValueIntentServiceModel}.
     *
     * @param currentState The current state of the state machine.
     * @param sessionModel The session model.
     * @param similarities      The found similarities.
     * @throws IllegalArgumentException If the any of the parameters is {@code null} or empty.
     */
    public ValueInputIntentModel(String currentState, SessionModel sessionModel, Collection<Similarity<Displayable>> similarities) {
        super(currentState, sessionModel);
        this.similarities = Collections.unmodifiableCollection(similarities);
    }

    /**
     * Instantiates a new instance of class {@link DefaultValueIntentServiceModel}.
     *
     * @param currentState      The current state of the state machine.
     * @param locale            The display locale.
     * @param analysisSituation The analysis situation.
     * @param operation         The operation the user wants to perform.
     * @param sessionModel      The session model.
     * @param userInput         The user input.
     * @param displayData       The data displayed to the user.
     * @param similarities      The found similarities.
     * @throws IllegalArgumentException If the any of the parameters (except {@code locale} and {@code operation}) is {@code null} or empty.
     */
    public ValueInputIntentModel(String currentState, Locale locale, EngineAnalysisSituation analysisSituation, Event operation, SessionModel sessionModel, String userInput, Display displayData, Collection<Similarity<Displayable>> similarities) {
        super(currentState, locale, analysisSituation, operation, sessionModel, userInput, displayData);
        this.similarities = Collections.unmodifiableCollection(similarities);
    }

    /**
     * Gets the similarities.
     *
     * @return the similarities
     */
    public Collection<Similarity<Displayable>> getSimilarities() {
        return similarities;
    }

    /**
     * Gets similarity with the highest score.
     * If there are no similarities available, {@code null} will be returned.
     *
     * @return the top similarity
     */
    public Similarity<Displayable> getTopSimilarity() {
        if (this.similarities == null) return null;
        return this.similarities.stream().sorted().findFirst().orElse(null);
    }
}
