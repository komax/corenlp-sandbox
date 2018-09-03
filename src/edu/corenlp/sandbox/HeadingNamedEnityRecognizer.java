package edu.corenlp.sandbox;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.stream.Stream;

import static edu.corenlp.sandbox.Utils.readTextfile;

public class HeadingNamedEnityRecognizer {

    private final StanfordCoreNLP pipeline;

    public HeadingNamedEnityRecognizer() {
        // Store set up for the pipeline as properties
        var props = new Properties();

        // Set up annotators from CoreNLP.
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");

        // Construct pipeline.
        this.pipeline = new StanfordCoreNLP(props);
    }

    public void analyzeTextfile(String pathToTextfile) {
        var path = Paths.get(pathToTextfile);
        this.analyzeTextfile(path);
        return;
    }

    public void analyzeTextfile(Path pathToPaper) {
        try {
            // Read the text file.
            var text = readTextfile(pathToPaper+ "/plain_text.txt");
            // Create a document to process.
            var document = new CoreDocument(text);

            // Annotate the text.
            pipeline.annotate(document);

            writeNERMentions(pathToPaper + "/ner-mentions.txt", document);
        } catch (IOException e) {
            System.out.println(pathToPaper);
            throw new RuntimeException(e);
        }
        return;
    }

    public static void writeNERMentions(String outputPath, CoreDocument doc) throws IOException {
        var outputStringBuilder = new StringBuilder("NER mentions:\n");

        for (var em : doc.entityMentions()) {
            outputStringBuilder.append("\t detected entity: ");
            outputStringBuilder.append(em.text());
            outputStringBuilder.append('\t');
            outputStringBuilder.append(em.entityType());
            outputStringBuilder.append('\n');
            outputStringBuilder.append("\t The corresponding sentence: \t");
            outputStringBuilder.append(em.sentence());
            outputStringBuilder.append('\n');

        }

        var path = Paths.get(outputPath);
        Files.write(path, outputStringBuilder.toString().getBytes());
    }


    public void handleAllPapers(String pathName) throws IOException {
        var path = Paths.get(pathName);
        var parentsNameCount = path.getNameCount();

        Files.walk(path)
                // Handle only directories.
                .filter(Files::isDirectory)
                // filter subdirectories of one level beneath.
                .filter(d -> d.getNameCount() == parentsNameCount + 1)
                // Analyze each paper.
                .forEach(p -> {this.analyzeTextfile(p.toString());
                        System.out.println("Analyzed: "+p); });
        return;
    }

    public static void main(String[] args) throws IOException {

//        if (args.length < 1) {
//            throw new IllegalArgumentException("Need to supply a path to a text file as an argument");
//        }
//        var path = args[0];

        var nerRecognizer = new HeadingNamedEnityRecognizer();
        nerRecognizer.handleAllPapers("/Users/mk21womu/Desktop/table_extraction_resources/output");
//        nerRecognizer.analyzeTextfile(path);


        System.exit(0);
    }
}
