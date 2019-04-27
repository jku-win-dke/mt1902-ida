package at.jku.dke.ida.shared.models;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * A comparative analysis situation allows to model comparison. It joins two analysis situations and relates
 * both by a score definition.
 */
public class ComparativeAnalysisSituation extends AnalysisSituation {

    private NonComparativeAnalysisSituation contextOfInterest; // 1. a non-comparative analysis situation as context of interest
    private NonComparativeAnalysisSituation contextOfComparison;  // 2. a non-comparative analysis situation as context of comparison
    private Set<Pair<String, String>> joinConditions; // 3. a non-empty set of join conditions
    private Set<String> scores; // 4. a non-empty set of scores
    private Set<String> scoreFilters; // 5. a possibly empty set of score filters

    /**
     * Instantiates a new instance of class {@linkplain ComparativeAnalysisSituation}.
     */
    public ComparativeAnalysisSituation() {
        this.contextOfInterest = null;
        this.contextOfComparison = null;
        this.joinConditions = new HashSet<>();
        this.scores = new HashSet<>();
        this.scoreFilters = new HashSet<>();
    }

    /**
     * Gets the context of interest.
     *
     * @return the context of interest
     */
    public NonComparativeAnalysisSituation getContextOfInterest() {
        return contextOfInterest;
    }

    /**
     * Sets the context of interest.
     *
     * @param contextOfInterest the context of interest
     */
    public void setContextOfInterest(NonComparativeAnalysisSituation contextOfInterest) {
        this.contextOfInterest = contextOfInterest;
    }

    /**
     * Gets the context of comparison.
     *
     * @return the context of comparison
     */
    public NonComparativeAnalysisSituation getContextOfComparison() {
        return contextOfComparison;
    }

    /**
     * Sets the context of comparison.
     *
     * @param contextOfComparison the context of comparison
     */
    public void setContextOfComparison(NonComparativeAnalysisSituation contextOfComparison) {
        this.contextOfComparison = contextOfComparison;
    }

    // region --- JoinConditions ---

    /**
     * Gets the join conditions as unmodifiable set.
     *
     * @return the join conditions
     */
    public Set<Pair<String, String>> getJoinConditions() {
        return Collections.unmodifiableSet(joinConditions);
    }

    /**
     * Sets the join conditions.
     *
     * @param joinConditions the join conditions
     * @throws IllegalArgumentException If {@code joinConditions} is empty or {@code null}.
     */
    public void setJoinConditions(Set<Pair<String, String>> joinConditions) {
        if (joinConditions == null || joinConditions.isEmpty())
            throw new IllegalArgumentException("joinConditions must not be empty");
        this.joinConditions = joinConditions;
    }

    /**
     * Adds the specified join condition if it is not already present.
     *
     * @param cond The join condition to be added to the set.
     * @return {@code true} if this set did not already contain the specified condition
     * @see Set#add(Object)
     */
    public boolean addJoinCondition(Pair<String, String> cond) {
        return joinConditions.add(cond);
    }

    /**
     * Removes the specified join condition if it is present.
     *
     * @param cond The join condition to be removed from the set, if present.
     * @return {@code true} if the set contained the specified condition
     * @see Set#remove(Object)
     */
    public boolean removeJoinCondition(Pair<String, String> cond) {
        return joinConditions.remove(cond);
    }
    // endregion

    // region --- Scores ---

    /**
     * Gets the scores as unmodifiable set.
     *
     * @return the scores
     */
    public Set<String> getScores() {
        return Collections.unmodifiableSet(scores);
    }

    /**
     * Sets the scores.
     *
     * @param scores the scores
     * @throws IllegalArgumentException If {@code scores} is empty or {@code null}.
     */
    public void setScores(Set<String> scores) {
        if (scores == null || scores.isEmpty()) throw new IllegalArgumentException("scores must not be empty");
        this.scores = scores;
    }

    /**
     * Adds the specified score if it is not already present.
     *
     * @param score The score to be added to the set.
     * @return {@code true} if this set did not already contain the specified score
     * @see Set#add(Object)
     */
    public boolean addScore(String score) {
        return scores.add(score);
    }

    /**
     * Removes the specified score if it is present.
     *
     * @param score The score to be removed from the set, if present.
     * @return {@code true} if the set contained the specified score
     * @see Set#remove(Object)
     */
    public boolean removeScore(String score) {
        return scores.remove(score);
    }
    // endregion

    // region --- ScoreFilters ---

    /**
     * Gets score filters as unmodifiable set.
     *
     * @return the score filters
     */
    public Set<String> getScoreFilters() {
        return Collections.unmodifiableSet(scoreFilters);
    }

    /**
     * Sets score filters (may be empty).
     *
     * @param scoreFilters the score filters
     */
    public void setScoreFilters(Set<String> scoreFilters) {
        this.scoreFilters = Objects.requireNonNullElseGet(scoreFilters, HashSet::new);
    }

    /**
     * Adds the specified score filter if it is not already present.
     *
     * @param scoref The score filter to be added to the set.
     * @return {@code true} if this set did not already contain the specified score filter
     * @see Set#add(Object)
     */
    public boolean addScoreFilter(String scoref) {
        return scoreFilters.add(scoref);
    }

    /**
     * Removes the specified score filter if it is present.
     *
     * @param scoref The score filter to be removed from the set, if present.
     * @return {@code true} if the set contained the specified score filter
     * @see Set#remove(Object)
     */
    public boolean removeScoreFilter(String scoref) {
        return scoreFilters.remove(scoref);
    }
    // endregion

    @Override
    public boolean isExecutable() {
        return contextOfInterest != null && contextOfInterest.isExecutable() &&
                contextOfComparison != null && contextOfComparison.isExecutable() &&
                !joinConditions.isEmpty() && joinConditions.stream().allMatch(jc -> StringUtils.isNotBlank(jc.getLeft()) && StringUtils.isNotBlank(jc.getRight())) &&
                !scores.isEmpty();
    }

    @Override
    public boolean isCubeDefined() {
        return contextOfInterest != null && contextOfInterest.isCubeDefined() &&
                contextOfComparison != null && contextOfComparison.isCubeDefined();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ComparativeAnalysisSituation.class.getSimpleName() + "[", "]")
                .add("contextOfInterest=" + contextOfInterest)
                .add("contextOfComparison=" + contextOfComparison)
                .add("joinConditions=" + joinConditions)
                .add("scores=" + scores)
                .add("scoreFilters=" + scoreFilters)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ComparativeAnalysisSituation that = (ComparativeAnalysisSituation) o;
        return Objects.equals(contextOfInterest, that.contextOfInterest) &&
                Objects.equals(contextOfComparison, that.contextOfComparison) &&
                Objects.equals(joinConditions, that.joinConditions) &&
                Objects.equals(scores, that.scores) &&
                Objects.equals(scoreFilters, that.scoreFilters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), contextOfInterest, contextOfComparison, joinConditions, scores, scoreFilters);
    }
}