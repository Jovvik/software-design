package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueryServletTest extends TestCommon {
    private QueryServlet queryServlet;

    protected QueryServletTest() throws SQLException {
    }

    @BeforeEach
    @Override
    void setup() throws IOException, SQLException {
        super.setup();
        queryServlet = new QueryServlet();
    }

    @Test
    void invalidQuery() throws IOException {
        when(request.getParameter("command")).thenReturn("invalid");
        queryServlet.doGet(request, response);
        assertBodyEqualsWithoutHtml("Unknown command: invalid\n");
    }

    @Test
    void maxQueryEmpty() throws IOException {
        when(request.getParameter("command")).thenReturn("max");
        queryServlet.doGet(request, response);
        assertBodyEquals("<h1>Product with max price: </h1>");
    }

    @Test
    void maxQuery() throws IOException, SQLException {
        addSampleProducts(2);
        when(request.getParameter("command")).thenReturn("max");
        queryServlet.doGet(request, response);
        assertBodyEquals("<h1>Product with max price: </h1>\nproduct_2\t2</br>");
    }

    @Test
    void minQueryEmpty() throws IOException {
        when(request.getParameter("command")).thenReturn("min");
        queryServlet.doGet(request, response);
        assertBodyEquals("<h1>Product with min price: </h1>");
    }

    @Test
    void minQuery() throws IOException, SQLException {
        addSampleProducts(2);
        when(request.getParameter("command")).thenReturn("min");
        queryServlet.doGet(request, response);
        assertBodyEquals("<h1>Product with min price: </h1>\nproduct_1\t1</br>");
    }

    @Test
    void sumQueryEmpty() throws IOException {
        when(request.getParameter("command")).thenReturn("sum");
        queryServlet.doGet(request, response);
        assertBodyEquals("Summary price: \n0");
    }

    @Test
    void sumQuery() throws IOException, SQLException {
        addSampleProducts(2);
        when(request.getParameter("command")).thenReturn("sum");
        queryServlet.doGet(request, response);
        assertBodyEquals("Summary price: \n3");
    }

    @Test
    void countQueryEmpty() throws IOException {
        when(request.getParameter("command")).thenReturn("count");
        queryServlet.doGet(request, response);
        assertBodyEquals("Number of products: \n0");
    }

    @Test
    void countQuery() throws IOException, SQLException {
        addSampleProducts(2);
        when(request.getParameter("command")).thenReturn("count");
        queryServlet.doGet(request, response);
        assertBodyEquals("Number of products: \n2");
    }
}