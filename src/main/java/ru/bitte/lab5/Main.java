package ru.bitte.lab5;

import org.xml.sax.SAXException;
import ru.bitte.lab5.exceptions.ElementParsingInFileException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * The main class of the program.
 */
public class Main {
    /**
     * The starting point of the program.
     * This method creates a new terminal with elements read from a file and starts it. The name of the file is supplied
     * from an environment variable called "COLLECTION".
     * @param args command line arguments
     */
    public static void main(String[] args) {
        String envVarName = "COLLECTION";
        if (System.getenv().containsKey(envVarName) && !System.getenv().get(envVarName).equals("")) {
            String fileName = System.getenv().get(envVarName);
            try {
                Terminal terminal = new Terminal(new File(fileName));
                terminal.start();
            } catch (IOException | SAXException e) {
                System.out.println("Error reading from a file: " + e.getMessage());
            } catch (ParserConfigurationException | TransformerConfigurationException e) {
                System.out.println("Configuration error: " + e.getMessage());
            } catch (ElementParsingInFileException e) {
                System.out.println("Problem parsing objects from the file: " + e.getMessage());
            }
        } else {
            System.out.println("The \"COLLECTION\" environment variable is not set or empty, quitting...");
        }
    }
}
