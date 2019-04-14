package at.jku.dke.inga.app.ruleset.csp;

import at.jku.dke.inga.app.ruleset.csp.domain.AnalysisSituation;
import at.jku.dke.inga.app.ruleset.csp.domain.AnalysisSituationElement;
import at.jku.dke.inga.data.models.CubeSimilarity;
import at.jku.dke.inga.data.models.DimensionSimilarity;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Contains helpers that are used in the CSP-rules files.
 */
public final class RuleHelpers {
    /**
     * Prevents creation of instances of this class.
     */
    private RuleHelpers() {
    }

    /**
     * Returns whether all elements are in the specified cube.
     * <p>
     * If {@code elem} is {@code null} or empty, {@code true} will be returned.
     *
     * @param cube The cube.
     * @param elem The cube elements to check.
     * @return {@code true} if all elements are in the specified cube; {@code false} otherwise.
     */
    public static boolean allInCube(String cube, AnalysisSituationElement elem) {
        if (elem == null) return true;
        if (elem.getElements().isEmpty()) return true;

        return elem.getElements().stream()
                .allMatch(x -> x.getCube().equals(cube));
    }

    /**
     * Returns whether there is only at maximum one element per dimension.
     * <p>
     * If {@code elem} is {@code null}, empty or contains no {@link DimensionSimilarity} objects, {@code true} will be returned.
     * If {@code elem} contains elements which are no instances of {@link DimensionSimilarity}, {@code false} will be returned.
     *
     * @param elem The elements.
     * @return {@code true} if there is maximum one element per dimension; {@code false} otherwise.
     */
    public static boolean maximumOneElementPerDimension(AnalysisSituationElement elem) {
        if (elem == null) return true;
        if (elem.getElements().isEmpty()) return true;
        if (elem.getElements().stream().noneMatch(x -> x instanceof DimensionSimilarity)) return true;
        if (elem.getElements().stream().anyMatch(x -> !(x instanceof DimensionSimilarity))) return false;

        return elem.getElements().stream()
                .map(x -> ((DimensionSimilarity) x).getDimension())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .values().stream()
                .allMatch(x -> x <= 1);
    }

    public static boolean multipleAssignmentsPerTerm(AnalysisSituation as) {
        if (as == null) return false;

        return as.getAllSimilarities().stream()
                .map(CubeSimilarity::getTerm)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .values().stream()
                .anyMatch(x -> x > 1);
    }

    public static int getMultipleAssignmentsPerTermScore(AnalysisSituation as) {
        if (as == null) return 0;

        // Build map
        Map<String, List<CubeSimilarity>> similarityMap = as.getAllSimilarities().stream().collect(Collectors.groupingBy(CubeSimilarity::getTerm));
        if (similarityMap.values().stream().allMatch(x -> x.size() <= 1)) return 0;

        // Calculate
        return (int) ((similarityMap.values().stream()
                .filter(x -> x.size() > 1)
                .flatMap(Collection::stream)
                .mapToDouble(CubeSimilarity::getScore)
                .map(d -> 1 - d)
                .sum() * -1) * 10_000d);
    }

    public static boolean containsDuplicates(AnalysisSituation as) {
        if (as == null) return false;
        return as.getAllSimilarities().stream()
                .collect(Collectors.groupingBy(CubeSimilarity::getElement, Collectors.counting()))
                .values()
                .stream()
                .anyMatch(x -> x > 1);
    }
}
