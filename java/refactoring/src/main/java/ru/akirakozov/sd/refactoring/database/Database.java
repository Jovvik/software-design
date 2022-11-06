package ru.akirakozov.sd.refactoring.database;

import java.sql.*;
import java.util.*;

public abstract class Database {

    private Database() {
    }

    private static final String DB_URL = "jdbc:sqlite:test.db";

    public static void addProduct(Product product) throws SQLException {
        try (Connection c = DriverManager.getConnection(DB_URL)) {
            // todo: remove possibility of sql injection
            String sql = String.format("INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"%s\",%d)", product.getName(),
                    product.getPrice());
            try (Statement stmt = c.createStatement()){
                stmt.executeUpdate(sql);
            }
        }
    }

    private static Product productFromResultSet(ResultSet rs) throws SQLException {
        return new Product(rs.getString("name"), rs.getInt("price"));
    }

    private static <R> R runQuery(String query, QueryProcessor<R> processor) throws SQLException {
        try (Connection c = DriverManager.getConnection(DB_URL)) {
            try (Statement stmt = c.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(query)) {
                    return processor.process(rs);
                }
            }
        }
    }

    private static List<Product> queryList(String query) throws SQLException {
        return runQuery(query, rs -> {
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                products.add(productFromResultSet(rs));
            }
            return products;
        });
    }

    private static Optional<Product> querySingle(String query) throws SQLException {
        return runQuery(query, rs -> {
            if (rs.next()) {
                return Optional.of(productFromResultSet(rs));
            } else {
                return Optional.empty();
            }
        });
    }

    public static List<Product> getProducts() throws SQLException {
        return queryList("SELECT * FROM PRODUCT");
    }

    public static Optional<Product> getCheapestProduct() throws SQLException {
        return querySingle("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");
    }

    public static Optional<Product> getMostExpensiveProduct() throws SQLException {
        return querySingle("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");
    }

    public static long getProductsCount() throws SQLException {
        return runQuery("SELECT COUNT(*) FROM PRODUCT", rs -> {
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                return 0L;
            }
        });
    }

    public static long getProductsSum() throws SQLException {
        return runQuery("SELECT SUM(price) FROM PRODUCT", rs -> {
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                return 0L;
            }
        });
    }
}
