package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.database.Database;
import ru.akirakozov.sd.refactoring.database.Product;
import ru.akirakozov.sd.refactoring.response.ResponseWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseWriter responseWriter = new ResponseWriter(response);
        try {
            responseWriter.writeHeader();
            for (Product product : Database.getProducts()) {
                responseWriter.writeProduct(product);
            }
            responseWriter.writeFooter();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        responseWriter.writeOk();
    }
}
