package pl.sggw.handlers;

import pl.sggw.model.Book;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class PostHandler {
    public static String returnResource(String start, HashMap<String, String> requestBody,
                                        ConcurrentHashMap<Integer, Book> booksDictionary)
            throws IOException {
        String resource = ResourceHandler.backToIndex();

        if (start.isEmpty()) {
            return resource;
        } else {
            resource = getAction(requestBody, booksDictionary, start);
        }

        return resource;
    }

    private static String getAction(HashMap<String, String> requestBody,
                                    ConcurrentHashMap<Integer, Book> booksDictionary,
                                    String resource)
            throws IOException {
        switch (resource) {
            case "addBookAction":
                if (booksDictionary.isEmpty()) {
                    int bookId = 1;
                    resource = getUpdateBookAction(requestBody, booksDictionary, bookId);
                } else {
                    int highestBookId = Collections.max(booksDictionary.keySet());
                    resource = getUpdateBookAction(requestBody, booksDictionary, highestBookId + 1);
                }
                break;
            case "clearBooksAction":
                resource = getClearBookAction(booksDictionary);
                break;
            case "updateBookAction":
                resource = getUpdateBookAction(requestBody, booksDictionary, Integer.parseInt(requestBody.get("id")));
        }
        return resource;
    }

    private static String getUpdateBookAction(HashMap<String, String> requestBody,
                                              ConcurrentHashMap<Integer, Book> booksDictionary, int id)
            throws IOException {
        String resource;
        Book book = getBook(requestBody, id);
        booksDictionary.put(id, book);
        resource = GetHandler.returnResource("books.html", booksDictionary);
        return resource;
    }

    private static String getClearBookAction(ConcurrentHashMap<Integer, Book> booksDictionary)
            throws IOException {
        String resource;
        booksDictionary.clear();
        resource = GetHandler.returnResource("books.html", booksDictionary);
        return resource;
    }

    private static Book getBook(HashMap<String, String> requestBody, int i) {
        return new Book(i, requestBody.get("title"), requestBody.get("authorName"),
                requestBody.get("authorSurname"));
    }
}
