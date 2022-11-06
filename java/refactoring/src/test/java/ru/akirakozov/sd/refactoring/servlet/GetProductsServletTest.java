package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(MockitoExtension.class)
class GetProductsServletTest extends TestCommon {
    private GetProductsServlet getProductsServlet;

    protected GetProductsServletTest() throws SQLException {
    }

    @BeforeEach
    @Override
    void setup() throws IOException, SQLException {
        super.setup();
        getProductsServlet = new GetProductsServlet();
    }

    @Test
    void empty() throws IOException {
        getProductsServlet.doGet(request, response);
        assertBodyEqualsWithoutNewline("");
    }

    @Test
    void oneProduct() throws IOException, SQLException {
        addSampleProducts(1);
        getProductsServlet.doGet(request, response);
        assertBodyEquals("product_1\t1</br>");
    }

    @Test
    void twoProducts() throws IOException, SQLException {
        addSampleProducts(2);
        getProductsServlet.doGet(request, response);
        assertBodyEquals("product_1\t1</br>\nproduct_2\t2</br>");
    }
}