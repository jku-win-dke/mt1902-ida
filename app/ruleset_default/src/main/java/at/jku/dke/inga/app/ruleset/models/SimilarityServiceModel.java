package at.jku.dke.inga.app.ruleset.models;

import at.jku.dke.inga.rules.models.DroolsServiceModel;
import at.jku.dke.inga.shared.models.AnalysisSituation;
import edu.stanford.nlp.pipeline.CoreDocument;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Model used by {@link at.jku.dke.inga.app.ruleset.drools.SimilarityService}.
 */
public class SimilarityServiceModel extends DroolsServiceModel {

    private final Set<WordGroup> wordGroups;

    /**
     * Instantiates a new instance of class {@link SimilarityServiceModel}.
     *
     * @param locale            The display locale.
     * @param analysisSituation The analysis situation.
     * @param additionalData    Additional data.
     * @param wordGroups        The word groups.
     * @throws IllegalArgumentException If {@code analysisSituation} is {@code null} or empty.
     */
    public SimilarityServiceModel(Locale locale, AnalysisSituation analysisSituation, Map<String, Object> additionalData, Set<WordGroup> wordGroups) {
        super("NONE", locale, analysisSituation, "nlp", additionalData);
        this.wordGroups = Collections.unmodifiableSet(wordGroups);
    }

    /**
     * Instantiates a new instance of class {@link WordGroupsServiceModel}.
     *
     * @param currentState      The current state.
     * @param locale            The display locale.
     * @param analysisSituation The analysis situation.
     * @param operation         The operation.
     * @param additionalData    Additional data.
     * @param wordGroups        The word groups.
     * @throws IllegalArgumentException If {@code analysisSituation} is {@code null} or empty.
     */
    public SimilarityServiceModel(String currentState, Locale locale, AnalysisSituation analysisSituation, String operation, Map<String, Object> additionalData, Set<WordGroup> wordGroups) {
        super(currentState, locale, analysisSituation, operation, additionalData);
        this.wordGroups = Collections.unmodifiableSet(wordGroups);
    }

    /**
     * Gets the word groups for which to find similarities in the schema.
     *
     * @return The word groups.
     */
    public Set<WordGroup> getWordGroups() {
        return wordGroups;
    }
}