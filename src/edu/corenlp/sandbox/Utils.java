package edu.corenlp.sandbox;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.XMLOutputter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {
    public static String readTextfile(String path) throws IOException {
        return readTextfile(Paths.get(path));
    }

    public static String readTextfile(Path path) throws IOException {
        var lines = Files.readAllLines(path);
        return lines.stream().reduce("", String::concat);
    }

    public static void writeXMLfromDocument(String outputFile, CoreDocument doc, StanfordCoreNLP pipeline) throws IOException {
        var xmlDoc = XMLOutputter.annotationToDoc(doc.annotation(), pipeline);
        var xml = xmlDoc.toXML().getBytes();
        var path = Paths.get(outputFile);
        Files.write(path, xml);
    }

}
