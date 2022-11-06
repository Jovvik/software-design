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
        ResponseWriter responseWriter = new ResponseWriter(response);

        switch (command) {
            case "max":
                responseWriter.writeHeader();
                responseWriter.writeHeading("Product with max price: ");
                Optional<Product> maxProduct = Database.getMostExpensiveProduct();
                if (maxProduct.isPresent()) {
                    responseWriter.writeProduct(maxProduct.get());
                }
                responseWriter.writeFooter();
                break;
            case "min":
                responseWriter.writeHeader();
                responseWriter.writeHeading("Product with min price: ");
                Optional<Product> minProduct = Database.getCheapestProduct();
                if (minProduct.isPresent()) {
                    responseWriter.writeProduct(minProduct.get());
                }
                responseWriter.writeFooter();
                break;
            case "sum":
                responseWriter.writeHeader();
                responseWriter.writeln("Summary price: ");
                responseWriter.writeln(String.valueOf(Database.getProductsSum()));
                responseWriter.writeFooter();
                break;
            case "count":
                responseWriter.writeHeader();
                responseWriter.writeln("Number of products: ");
                responseWriter.writeln(String.valueOf(Database.getProductsCount()));
                responseWriter.writeFooter();
                break;
            default:
                responseWriter.writeln("Unknown command: " + command);
                break;
        }

        responseWriter.writeOk();
    }
}
