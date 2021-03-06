package at.jku.dke.ida.nlp;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.semgrex.SemgrexMatcher;
import edu.stanford.nlp.semgraph.semgrex.SemgrexPattern;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import edu.stanford.nlp.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class provides methods to annotate a user input and to query the annotated text.
 */
public final class NLPProcessor {

    private static final Map<String, StanfordCoreNLP> pipelines = new HashMap<>(2);

    private static StanfordCoreNLP getPipeline(String language) {
        if (pipelines.containsKey(language)) return pipelines.get(language);

        // Set up pipeline properties
        Properties props;
        if (language.equals("de")) {
            props = StringUtils.argsToProperties("-props", "StanfordCoreNLP-german.properties");
            //props.setProperty("parse.model", "edu/stanford/nlp/models/srparser/germanSR.ser.gz");
        } else {
            props = new Properties();
            //props.setProperty("parse.model", "edu/stanford/nlp/models/srparser/englishSR.ser.gz");
        }
        props.setProperty("annotators", "tokenize,ssplit,pos,parse,depparse");

        // Build pipeline
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        pipelines.put(language, pipeline);

        return pipeline;
    }

    /**
     * Annotates the text using StanfordNLP using the annotators: Tokenize, Sentence Split,
     * Part-Of-Speech, Constituency Parsing, Dependency Parsing.
     *
     * @param language The language (must be {@code en} or {@code de}).
     * @param text     The text to annotate.
     * @return The annotated document.
     * @throws IllegalArgumentException If any of the parameters is {@code null} or if {@code language} is invalid.
     */
    public static CoreDocument annotate(String language, String text) {
        if (language == null) throw new IllegalArgumentException("language must not be null");
        if (text == null) throw new IllegalArgumentException("text must not be null");
        if (!language.equals("en") && !language.equals("de"))
            throw new IllegalArgumentException("language must be 'en' or 'de'");

        // build pipeline
        StanfordCoreNLP pipeline = getPipeline(language);
        CoreDocument document = new CoreDocument(text);

        // Annotate
        pipeline.annotate(document);
        return document;
    }

    /**
     * Executes a Tregex Matcher using the specified pattern on the given document.
     *
     * <p>
     * The pattern must use named nodes. The words are built by sorting the node names.
     * </p>
     *
     * @param doc     The document.
     * @param pattern The pattern.
     * @return Found results
     */
    public static Set<String> executeTregex(CoreDocument doc, String pattern) {
        if (doc == null) throw new IllegalArgumentException("doc must not be null");
        if (pattern == null) throw new IllegalArgumentException("pattern must not be null");

        Set<String> results = new HashSet<>();
        TregexPattern tregex = TregexPattern.compile(pattern);
        for (CoreSentence sentence : doc.sentences()) {
            TregexMatcher matcher = tregex.matcher(sentence.constituencyParse());
            while (matcher.find()) {
                results.add(matcher.getNodeNames()
                        .stream()
                        .sorted()
                        .flatMap(x -> matcher.getNode(x).getLeaves().stream())
                        .map(Tree::value)
                        .collect(Collectors.joining(" ")));
            }
        }

        return results;
    }

    /**
     * Executes a Semregex Matcher using the specified pattern on the given document.
     *
     * <p>
     * The pattern must use named nodes. The words are built by sorting the node names.
     * </p>
     *
     * @param doc     The document.
     * @param pattern The pattern.
     * @return Found results
     */
    public static Set<String> executeSemgrex(CoreDocument doc, String pattern) {
        if (doc == null) throw new IllegalArgumentException("doc must not be null");
        if (pattern == null) throw new IllegalArgumentException("pattern must not be null");

        Set<String> results = new HashSet<>();
        SemgrexPattern semgrex = SemgrexPattern.compile(pattern);
        for (CoreSentence sentence : doc.sentences()) {
            SemgrexMatcher matcher = semgrex.matcher(sentence.dependencyParse());
            while (matcher.find()) {
                results.add(matcher.getNodeNames()
                        .stream()
                        .sorted()
                        .map(x -> matcher.getNode(x).value())
                        .collect(Collectors.joining(" ")));
            }
        }

        return results;
    }

    /**
     * Prevents creation of instances of this class.
     */
    private NLPProcessor() {
    }
}
