package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.database.Database;
import ru.akirakozov.sd.refactoring.database.Product;
import ru.akirakozov.sd.refactoring.response.ResponseWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            doGetImpl(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void doGetImpl(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String command = request.getParameter("command");

        switch (command) {
            case "max" -> ResponseWriter.writeOptionalProduct(
                    "Product with max price",
                    Database.getMostExpensiveProduct(),
                    response);
            case "min" -> ResponseWriter.writeOptionalProduct(
                    "Product with min price",
                    Database.getCheapestProduct(),
                    response);
            case "sum" -> ResponseWriter.writeValue(
                    "Summary price",
                    String.valueOf(Database.getProductsSum()),
                    response);
            case "count" -> ResponseWriter.writeValue(
                    "Number of products",
                    String.valueOf(Database.getProductsCount()),
                    response);
            default -> ResponseWriter.writeln(
                    "Unknown command: " + command,
                    response);
        }
    }
}
