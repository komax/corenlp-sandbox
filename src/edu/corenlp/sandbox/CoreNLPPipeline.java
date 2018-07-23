package edu.corenlp.sandbox;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.XMLOutputter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class CoreNLPPipeline {
    public static String readTextfile(String path) throws IOException {
        var lines = Files.readAllLines(Paths.get(path));
        return lines.stream().reduce("", String::concat);
    }

    public static void writeXMLfromDocument(String outputFile, CoreDocument doc, StanfordCoreNLP pipeline) throws IOException {
        var xmlDoc = XMLOutputter.annotationToDoc(doc.annotation(), pipeline);
        var xml = xmlDoc.toXML().getBytes();
        var path = Paths.get(outputFile);
        Files.write(path, xml);
    }

    public static void main(String[] args) throws IOException{
        if (args.length < 1) {
            throw new IllegalArgumentException("Need to supply a path to a text file as an argument");
        }
        // Read the text file.
        var text = readTextfile(args[0]);

        // Store set up for the pipeline as properties.
        var props = new Properties();

        // Set up annotators from CoreNLP.
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,depparse");

        // Construct pipeline.
        var pipeline = new StanfordCoreNLP(props);
        // Create a document to process.
        var document = new CoreDocument(text);

        // Annotate the text.
        pipeline.annotate(document);

        // List of sentences.
        var sentences = document.sentences();

        // Get the first sentence.
        var firstSentence = sentences.get(0);

        // Obtain the semantic graph for the first sentence.
        var tree = firstSentence.dependencyParse();
        // Output on stdout the semantic graph.
        System.out.println(tree);

        // Write an xml file from the annotated document.
        var xmlDocPath = "./doc.xml";
        writeXMLfromDocument(xmlDocPath, document, pipeline);


    }
}
