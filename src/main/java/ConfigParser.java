/**
 * ConfigParser.java: A file parser class that reads configuration data from a file
 * into a program.
 */

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ConfigParser {
    private Map<String, String > config = new HashMap<>();

    public ConfigParser (String filename) {
        this(filename, "production");
    }

    public ConfigParser(String filename, String env) {
        // set the filename depending on the environment
        if (env == null) env = "production";
        switch (env.toLowerCase()) {
            case "staging":
                filename = filename.replace(".", "-staging.");
                break;
            case "development": case "dev":
                filename = filename.replace(".", "-dev.");
                break;
        }
        Path pathToFile = FileSystems.getDefault().getPath(filename); // Create a path object of the file
        System.out.println(pathToFile.toAbsolutePath().toString());
        // Check if users current working directory is the application folder or from the "java" folder
        if (!pathToFile.toAbsolutePath().toString().contains("src/main/java/"))
            pathToFile = FileSystems.getDefault().getPath("src/main/java/" + filename);

        if (!Files.exists(pathToFile)) throw new Error("This file is not present on the file system");
        this.config = fileToMap(pathToFile);

    }

    /**
     * returns configuration value
     * @param name
     * @return String
     */
    public String get(String name) {
        return config.get(name);
    }

    /**
     * fileToMap takes in a Path object and reads from the file associated with the path
     * the values in the file are parsed and stored in a map which is then returned
     * @param path
     * @return Map<String, String>
     */
    private final Map<String, String> fileToMap(Path path) {
        Map<String, String > config = new HashMap<>();

        try (Scanner input = new Scanner(path)) {
            String prefix = ""; // to be prefixed in front of application property names
            while (input.hasNext()) {
                try {
                    String line = input.nextLine();
                    if (line.length() > 1) {
                        if (line.startsWith("[")) {
                            prefix = line.substring(1, line.length() - 1) + ".";
                            line = input.nextLine();
                        }
                        String[] inputArr = line.split("=");
                        config.computeIfAbsent(prefix + inputArr[0], k -> inputArr[1]);
                    } else {
                        prefix = ""; // reset prefix to null when we leave application config block
                    }
                } catch (NoSuchElementException e) { // handles errors thrown by the scanner
                    System.err.println("Error reading from file! Terminating!\n");
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        } catch (IOException e) { // handles all IOExceptions
            System.err.println("Cannot open file! Terminating!\n");
            e.printStackTrace();
            System.exit(1);
        }
        return config;
    }

}
