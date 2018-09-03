package edu.corenlp.sandbox;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.io.IOException;
import java.util.Properties;

public class CoreNLPPipeline {

    public static void main(String[] args) throws IOException{
        if (args.length < 1) {
            throw new IllegalArgumentException("Need to supply a path to a text file as an argument");
        }
        // Read the text file.
        var text = Utils.readTextfile(args[0]);

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
        Utils.writeXMLfromDocument(xmlDocPath, document, pipeline);


    }
}
