package ru.akirakozov.sd.refactoring.response;

import ru.akirakozov.sd.refactoring.database.Product;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseWriter {

    private final HttpServletResponse response;

    public ResponseWriter(HttpServletResponse response) {
        this.response = response;
    }

    public void writeOk() {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void writeHeader() throws IOException {
        response.getWriter().println("<html><body>");
    }

    public void writeFooter() throws IOException {
        response.getWriter().println("</body></html>");
    }

    public void writeHeading(String heading) throws IOException {
        response.getWriter().println("<h1>" + heading + "</h1>");
    }

    public void writeln(String line) throws IOException {
        response.getWriter().println(line);
    }

    public void writeProduct(Product product) throws IOException {
        response.getWriter().println(product.getName() + "\t" + product.getPrice() + "</br>");
    }
}
