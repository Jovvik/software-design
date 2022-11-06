package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.akirakozov.sd.refactoring.database.Product;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddProductServletTest extends TestCommon {
    private AddProductServlet addProductServlet;

    protected AddProductServletTest() throws SQLException {
    }

    @BeforeEach
    @Override
    void setup() throws IOException, SQLException {
        super.setup();
        addProductServlet = new AddProductServlet();
    }

    private void assertDbContainsItems(List<Product> products) {
        try (Statement stmt = c.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT");
            for (Product product : products) {
                assertTrue(rs.next());
                assertEquals(product.getName(), rs.getString("name"));
                assertEquals(product.getPrice(), rs.getInt("price"));
            }
            assertFalse(rs.next());
        } catch (SQLException e) {
            fail(e);
        }
    }

    @Test
    void addOneProduct() throws IOException {
        when(request.getParameter("name")).thenReturn("product_1");
        when(request.getParameter("price")).thenReturn("1");
        addProductServlet.doGet(request, response);
        assertBodyEqualsWithoutHtml("OK\n");
        assertDbContainsItems(Collections.singletonList(new Product("product_1", 1)));
    }

    @Test
    void missingName() throws IOException {
        when(request.getParameter("name")).thenReturn(null);
        when(request.getParameter("price")).thenReturn("1");
        addProductServlet.doGet(request, response);
        assertBodyEqualsWithoutHtml("OK\n");
        assertDbContainsItems(Collections.singletonList(new Product("null", 1)));
    }

    @Test
    void missingPrice() throws IOException {
        Mockito.reset(response); // it will not be written to
        when(request.getParameter("name")).thenReturn("product_1");
        when(request.getParameter("price")).thenReturn(null);
        assertThrows(NumberFormatException.class, () -> addProductServlet.doGet(request, response));
        verify(response, never()).getWriter();
    }

    @Test
    void notOverwritingProduct() throws IOException {
        when(request.getParameter("name")).thenReturn("product_1");
        when(request.getParameter("price")).thenReturn("1");
        addProductServlet.doGet(request, response);
        assertBodyEqualsWithoutHtml("OK\n");
        assertDbContainsItems(Collections.singletonList(new Product("product_1", 1)));
        when(request.getParameter("name")).thenReturn("product_1");
        when(request.getParameter("price")).thenReturn("2");
        addProductServlet.doGet(request, response);
        assertBodyEqualsWithoutHtml("OK\nOK\n");
        assertDbContainsItems(List.of(new Product("product_1", 1), new Product("product_1", 2)));
    }
}