package pl.sggw.handlers;

import pl.sggw.model.Book;
import pl.sggw.model.BookToString;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class GetHandler {
    public static String returnResource(String start, ConcurrentHashMap<Integer, Book> booksDictionary)
            throws IOException {

        String resource = ResourceHandler.backToIndex();

        if (start.equals("favicon.ico") || start.isEmpty())
            return resource;

        try {
            resource = ResourceHandler.readFromPath(start.split("\\?", 2)[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return resource;
        }

        resource = detectResource(start, booksDictionary, resource);

        return resource;
    }

    private static String detectResource(String start, ConcurrentHashMap<Integer, Book> booksDictionary, String view) throws IOException {
        if (start.equals("books.html")) {
            view = getBooksResource(booksDictionary, view);
        } else if (start.startsWith("updateBook.html")) {
            view = getUpdateBookResource(start, booksDictionary, view);
        }
        return view;
    }

    private static String getUpdateBookResource(String start, ConcurrentHashMap<Integer, Book> booksDictionary, String view) throws IOException {
        String url = start.split("\\?", 2)[1];
        HashMap<String, String> parameters = RequestHandler.urlParameters(url);
        if (parameters.containsKey("id")) {
            view = updateBook(booksDictionary, parameters);
        }
        return view;
    }

    private static String updateBook(ConcurrentHashMap<Integer, Book> booksDictionary,
                                     HashMap<String, String> parameters)
            throws FileNotFoundException {
        String resource;
        Book book = booksDictionary.get(Integer.parseInt(parameters.get("id")));
        resource = ResourceHandler.readFromPath("updateBook.html");
        resource = formatResource(parameters, resource, book);
        return resource;
    }

    private static String formatResource(HashMap<String, String> parameters, String resource, Book book) {
        resource = String.format(resource, parameters.get("id"), book.getTitle(), book.getAuthorName(),
                book.getAuthorSurname());
        return resource;
    }

    private static String getBooksResource(ConcurrentHashMap<Integer, Book> booksDictionary, String view) {
        if (booksDictionary.isEmpty()) {
            view = String.format(view,"<p>Brak książek</p>");
        }
        String booksAsHtml = BookToString.databaseToHtml(booksDictionary);
        view = String.format(view, booksAsHtml);
        return view;
    }
}
