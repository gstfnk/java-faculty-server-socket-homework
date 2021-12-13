package pl.sggw.serializers;

import pl.sggw.model.Book;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class JSONDeserializer {
    public static String convertBookToString(Book toConvert) throws ClassNotFoundException, IllegalAccessException {
        Field[] fields = Class.forName(Book.class.getName()).getDeclaredFields();
        fields[0].setAccessible(true);
        StringBuilder out = new StringBuilder("  \"" + fields[0].get(toConvert) + "\": {\n");
        for (int i = 1; i < fields.length; i++) {
            fields[i].setAccessible(true);
            out.append("    \"").append(fields[i].getName()).append("\": \"").append(fields[i].get(toConvert)).append("\"");
            if (i != fields.length - 1) {
                out.append(",");
            }
            out.append("\n");
        }
        out.append("  }");
        System.out.println("cbts:" + out);
        return out.toString();
    }

    public static String convertConcurrentHashMapToString(ConcurrentHashMap<Integer, Book> bookConcurrentHashMap)
            throws ClassNotFoundException, IllegalAccessException {
        StringBuilder out = new StringBuilder("{\n");
        Collection<Book> books = bookConcurrentHashMap.values();
        int i = 0;
        for (Book toConvert : books) {
            out.append(convertBookToString(toConvert));
            if (i != books.size() - 1) {
                out.append(",");
            }
            out.append("\n");
            i++;
        }
        out.append("}");
        System.out.println("cchmts: " + out);
        return out.toString();
    }

    public static void createFileFromConcurrentHashMap(ConcurrentHashMap<Integer, Book> contentMap, String path)
            throws IOException, ClassNotFoundException, IllegalAccessException {
        String content = convertConcurrentHashMapToString(contentMap);
        // Deleting the contents of file without deleting the file itself:
        PrintWriter printWriter = new PrintWriter(path);
        printWriter.print("");
        printWriter.close();
        // Write to a file:
        BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
        writer.append(content);
        writer.close();
    }

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, IOException {
        Book book = new Book(13, "Tytuł", "Imie", "Nazwisko");
        //String val = convertBookToString(book);
        ConcurrentHashMap<Integer, Book> books = JSONSerializer.getBooksConcurrentHashMap(
                JSONSerializer.readJSON("src/main/resources/library.json"));
        Book toAdd2 = new Book(11, "1984", "George", "Orwell");
        books.remove(10);
        createFileFromConcurrentHashMap(books, "src/main/resources/library.json");
    }
}
