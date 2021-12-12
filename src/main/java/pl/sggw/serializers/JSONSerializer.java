package pl.sggw.serializers;

import pl.sggw.model.Book;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class JSONSerializer {

    public static String readJSON(String jsonPath) throws FileNotFoundException {
        File file = new File(jsonPath);
        Scanner fileReader = new Scanner(file);
        StringBuilder json = new StringBuilder();
        while (fileReader.hasNextLine()) {
            json.append(fileReader.nextLine());
        }
        fileReader.close();

        return json.toString();
    }

    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException {
        getBooksMap(readJSON("src/main/resources/library.json"));
    }

    private static String[] parseJsonToString(String json) {
        json = json.substring(1, json.length() - 1);
        String[] out = json.split("},");
        for (int i = 0; i < out.length - 1; i++) {
            out[i] += "}";
        }
        printArray(out);
        return out;
    }

    private static ConcurrentHashMap<Integer, Book> getBooksMap(String json) throws ClassNotFoundException {
        ConcurrentHashMap<Integer, Book> booksDictionary = new ConcurrentHashMap<>();
        String[] input = parseJsonToString(json);
        // Using reflection:
        String[] bookValues = new String[Class.forName(Book.class.getName()).getDeclaredFields().length - 1];

        for (String entry : input) {
            // "1": {    "title": "Diuna",    "authorName": "Frank",    "authorSurname": "Herbert"  }
            // "1":{    "title": "Diuna",    "authorName": "Frank",    "authorSurname": "Herbert"  }
            // ["1"], [{    "title": "Diuna",    "authorName": "Frank",    "authorSurname": "Herbert"  }}]
            String[] keyValuePair = entry.trim().split(":", 2);
            // 1
            int id = Integer.parseInt(keyValuePair[0].substring(1, keyValuePair[0].length() - 1));
            // "    "title": "Diuna",    "authorName": "Frank",    "authorSurname": "Herbert"  "
            String bookData = keyValuePair[1].trim().substring(1, keyValuePair[1].length() - 2).trim();

            String[] bookProperties = bookData.trim().split(",  ");
            for (int i = 0; i < bookProperties.length; i++) {
                String val = bookProperties[i].trim();
                bookProperties[i] = val;
            }

            for (int i = 0; i < bookValues.length; i++) {
                String value = bookProperties[i].split(":")[1];
                bookValues[i] = value.trim().substring(1, value.length() - 2);
            }

            System.out.println("Book values: " + Arrays.toString(bookValues));
            // TODO: Convert to java reflection:
            Book toPut = new Book(id, bookValues[0], bookValues[1], bookValues[2]);
            booksDictionary.put(id, toPut);
        }

        return booksDictionary;
    }

    private static void printArray(String[] array) {
        String toPrint = "";
        for (String string : array) {
            toPrint += string + "\n";
        }
        System.out.println(toPrint);
    }
}
