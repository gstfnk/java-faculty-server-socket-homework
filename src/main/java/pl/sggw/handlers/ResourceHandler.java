package pl.sggw.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ResourceHandler {

    public static String readFromPath(String path) throws FileNotFoundException {
        File resource = new File(path);
        StringBuilder stringBuilder = scanTheFile(resource);

        return stringBuilder.toString();
    }

    private static StringBuilder scanTheFile(File resource) throws FileNotFoundException {
        Scanner scanner = new Scanner(resource);

        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine());
        }

        scanner.close();
        return stringBuilder;
    }

    public static String backToIndex() throws FileNotFoundException {
        return readFromPath("index.html");
    }
}
