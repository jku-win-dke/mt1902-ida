package at.jku.dke.ida.data.repositories;

import at.jku.dke.ida.data.GraphDbConnection;
import at.jku.dke.ida.data.IRIValidator;
import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.models.labels.ComparativeLabel;
import at.jku.dke.ida.data.models.labels.ComparativeMeasureLabel;
import at.jku.dke.ida.data.repositories.base.SimpleCubeElementRepository;
import at.jku.dke.ida.shared.IRIConstants;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.rdf4j.query.BindingSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Repository for querying comparative measures.
 */
@Service
public class ComparativeMeasureRepository extends SimpleCubeElementRepository {

    /**
     * Instantiates a new instance of class {@linkplain ComparativeMeasureRepository}.
     *
     * @param connection The GraphDB connection service class.
     */
    @Autowired
    public ComparativeMeasureRepository(GraphDbConnection connection) {
        super(connection, IRIConstants.TYPE_COMPARATIVE_MEASURE, "repo_compmeasure", "comparative measures");
    }

    private List<ComparativeMeasureLabel> mapAllResultToLabel(String lang, Stream<BindingSet> stream) {
        return stream
                .map(x -> RepositoryHelpers.convertToComparativeMeasureLabel(lang, typeIri, x))
                .collect(Collectors.toList());
    }

    /**
     * Returns the labels of all elements of the type of this repository.
     *
     * @param lang The requested language.
     * @return List with elements labels in the requested language
     * @throws IllegalArgumentException If {@code lang} is {@code null} or blank.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    @Override
    public List<ComparativeMeasureLabel> getAllLabelsByLang(String lang) throws QueryException {
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");

        logger.debug("Querying labels of language {} for levels.", lang);

        return mapAllResultToLabel(lang, connection.getQueryResult(
                "/" + queryFolder + "/getAllLabelsByLang.sparql",
                s -> s.replaceAll("###LANG###", lang)
        ).stream());
    }

    /**
     * Returns the labels of all elements of the type of this repository,
     * except those specified in the exclusions collection.
     *
     * @param lang      The requested language.
     * @param exclusion The collection with IRIs to exclude from the result.
     * @return List with elements labels of the cube in the requested language except the specified ones
     * @throws IllegalArgumentException If {@code lang} or {@code exclusion} is {@code null} or blank.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    @Override
    public List<ComparativeMeasureLabel> getAllLabelsByLang(String lang, Collection<String> exclusion) throws QueryException {
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");
        if (exclusion == null) throw new IllegalArgumentException("excluded must not be null");
        if (exclusion.stream().map(IRIValidator::isValidAbsoluteIRI).anyMatch(x -> !x))
            throw new IllegalArgumentException("exclusion contains at least one invalid IRI");

        logger.debug("Querying labels of language {} for levels with exclusions.", lang);

        return mapAllResultToLabel(lang, connection.getQueryResult(
                "/" + queryFolder + "/getAllLabelsByLangExcept.sparql",
                s -> s.replaceAll("###LANG###", lang).replace("###NOTIN###", convertToFullIriString(exclusion))
        ).stream());
    }


    /**
     * Returns all comparative measures that belong to a comparative measure predicate.
     *
     * @param compMeasurePredicateIri Absolute IRI of the comparative measure predicate.
     * @return Set with element IRIs
     * @throws IllegalArgumentException If {@code compMeasurePredicateIri} is {@code null}, blank or an invalid IRI.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public Set<String> getAllByPredicate(String compMeasurePredicateIri) throws QueryException {
        if (StringUtils.isBlank(compMeasurePredicateIri)) throw new IllegalArgumentException("cubeIri must not be null nor empty");
        if (!IRIValidator.isValidAbsoluteIRI(compMeasurePredicateIri))
            throw new IllegalArgumentException("cubeIri must be an absolute IRI");

        logger.debug("Querying all {} of predicate {}.", pluralLogName, compMeasurePredicateIri);
        return mapResultToType(connection.getQueryResult(
                "/" + queryFolder + "/getAllByPredicate.sparql",
                s -> s.replaceAll("###PREDICATE###", compMeasurePredicateIri)).stream());
    }
}
