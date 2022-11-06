package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public abstract class TestCommon {
    @Mock
    protected HttpServletRequest request;

    @Mock
    protected HttpServletResponse response;

    protected StringWriter responseWriter;

    protected Connection c = DriverManager.getConnection("jdbc:sqlite:test.db");

    protected TestCommon() throws SQLException {
    }

    void setup() throws IOException, SQLException {
        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
        try (Statement stmt = c.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS PRODUCT");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)");
        }
    }

    protected void addSampleProducts(int productCount) throws SQLException {
        try (Statement stmt = c.createStatement()) {
            for (int i = 1; i <= productCount; i++) {
                stmt.executeUpdate("INSERT INTO PRODUCT " +
                        "(NAME, PRICE) VALUES (\"product_" + i + "\", " + i + ")");
            }
        }
    }

    private String getResponse() {
        return responseWriter.toString();
    }

    void assertBodyEquals(String expected) {
        assertBodyEqualsWithoutNewline(expected + "\n");
    }

    void assertBodyEqualsWithoutNewline(String expected) {
        assertEquals("<html><body>\n" + expected + "</body></html>\n", getResponse());
    }

    void assertBodyEqualsWithoutHtml(String expected) {
        assertEquals(expected, getResponse());
    }
}
