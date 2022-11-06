package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.database.Database;
import ru.akirakozov.sd.refactoring.database.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        if ("max".equals(command)) {
            response.getWriter().println("<html><body>");
            response.getWriter().println("<h1>Product with max price: </h1>");
            try {
                Optional<Product> product = Database.getMostExpensiveProduct();
                if (product.isPresent()) {
                    response.getWriter().println(product.get().getName() + "\t" + product.get().getPrice() + "</br>");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            response.getWriter().println("</body></html>");
        } else if ("min".equals(command)) {
            response.getWriter().println("<html><body>");
            response.getWriter().println("<h1>Product with min price: </h1>");
            try {
                Optional<Product> product = Database.getCheapestProduct();
                if (product.isPresent()) {
                    response.getWriter().println(product.get().getName() + "\t" + product.get().getPrice() + "</br>");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            response.getWriter().println("</body></html>");
        } else if ("sum".equals(command)) {
            response.getWriter().println("<html><body>");
            response.getWriter().println("Summary price: ");
            try {
                response.getWriter().println(Database.getProductsSum());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            response.getWriter().println("</body></html>");
        } else if ("count".equals(command)) {
            response.getWriter().println("<html><body>");
            response.getWriter().println("Number of products: ");
            try {
                response.getWriter().println(Database.getProductsCount());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            response.getWriter().println("</body></html>");
        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
