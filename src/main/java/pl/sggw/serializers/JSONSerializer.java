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

    public static void main(String[] args) throws FileNotFoundException {
        parseJsonToString(readJSON("src/main/resources/library.json"));
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

    private static ConcurrentHashMap<Integer, Book> getBooksMap(String json) {
        ConcurrentHashMap<Integer, Book> booksDictionary = new ConcurrentHashMap<>();
        String[] entries = parseJsonToString(json);

        String[] keyValuePair;
        int id;
        String bookData;
        String[] bookProperties;
        String[] bookValues = new String[3];
        Book book;
        for (String entry : entries) {
            System.out.println("---------");
            // "1": {    "title": "Diuna",    "authorName": "Frank",    "authorSurname": "Herbert"  }
            // "1":{    "title": "Diuna",    "authorName": "Frank",    "authorSurname": "Herbert"  }
            // ["1"], [{    "title": "Diuna",    "authorName": "Frank",    "authorSurname": "Herbert"  }}]
            keyValuePair = entry.trim().split(":", 2);
            printArray(keyValuePair);
            id = Integer.parseInt(keyValuePair[0].substring(1, keyValuePair[0].length() - 1));
            System.out.println(id);

            bookData = keyValuePair[1].trim().substring(1, keyValuePair[1].length() - 2);
            System.out.println(bookData);
            bookProperties = bookData.trim().split(",");
            System.out.println("properties");
            printArray(bookProperties);
            for (int i = 0; i < bookProperties.length; i++) {
                String value = bookProperties[i].split(":")[1];
                bookValues[i] = value.trim().substring(1, value.length() - 2);
            }
            System.out.println("values" + Arrays.toString(bookValues));

//            book = new Book(bookValues[0], bookValues[1], bookValues[2], id);
//            booksDictionary.put(id, book);
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
