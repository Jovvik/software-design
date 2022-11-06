package ru.akirakozov.sd.refactoring.response;

import ru.akirakozov.sd.refactoring.database.Product;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public abstract class ResponseWriter {

    public static void writeOk(HttpServletResponse response) {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private static void writeHeader(HttpServletResponse response) throws IOException {
        response.getWriter().println("<html><body>");
    }

    private static void writeFooter(HttpServletResponse response) throws IOException {
        response.getWriter().println("</body></html>");
    }

    private static void writeHeading(String heading, HttpServletResponse response) throws IOException {
        response.getWriter().println("<h1>" + heading + "</h1>");
    }

    public static void writeln(String line, HttpServletResponse response) throws IOException {
        writelnImpl(line, response);
        writeOk(response);
    }

    private static void writelnImpl(String line, HttpServletResponse response) throws IOException {
        response.getWriter().println(line);
    }

    private static void writeProduct(Product product, HttpServletResponse response) throws IOException {
        response.getWriter().println(product.getName() + "\t" + product.getPrice() + "</br>");
    }

    public static void writeOptionalProduct(String heading, Optional<Product> product, HttpServletResponse response) throws IOException {
        writeHeader(response);
        writeHeading(heading + ": ", response);
        if (product.isPresent()) {
            writeProduct(product.get(), response);
        }
        writeFooter(response);
        writeOk(response);
    }

    public static void writeProducts(List<Product> products, HttpServletResponse response) throws IOException {
        writeHeader(response);
        for (Product product : products) {
            writeProduct(product, response);
        }
        writeFooter(response);
        writeOk(response);
    }

    public static void writeValue(String title, String value, HttpServletResponse response) throws IOException {
        writeHeader(response);
        writeln(title + ": ", response);
        writeln(value, response);
        writeFooter(response);
        writeOk(response);
    }
}
