package data.internal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class used to handle XML reading and writing for the DataManager
 */
public class XMLHandler {

    private static final String COULD_NOT_CLOSE_FILES = "Could not close files";
    private static final String CANNOT_READ_XML_FILE = "Cannot read XML file";
    private static final String WRITE_FAILED = "Write Failed";

    /**
     * Writes a file of xml to the specified path
     * @param path path of where to write
     * @param rawXML xml to write to the file
     */
    public void writeToXML(String path, String rawXML) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(new File(path));
            fileWriter.write(rawXML);
        } catch (IOException exception) {
            System.out.println(WRITE_FAILED); //Debugging information, method only used internally
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                System.out.println(COULD_NOT_CLOSE_FILES);
            }
        }
    }

    /**
     * Reads xml from a file at a specified path
     * @param path location of the file
     * @return string of raw xml
     * @throws FileNotFoundException if file is not found
     */
    public String readFromXML(String path) throws FileNotFoundException {
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;
        StringBuilder rawXML = new StringBuilder();
        try {
            fileReader = new FileReader(path);
            bufferedReader = new BufferedReader(fileReader);
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                rawXML.append(currentLine);
            }
        } catch (IOException e) {
            System.out.println(CANNOT_READ_XML_FILE);
            throw new FileNotFoundException();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException ex) {
                System.out.println(COULD_NOT_CLOSE_FILES);
            }
        }
        return rawXML.toString();
    }
}
