package org.sales.taxes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sales.taxes.model.Product;
import org.sales.taxes.service.impl.SalesServiceImpl;
import org.springframework.mock.web.MockMultipartFile;

public class SalesServiceTest {

    private SalesServiceImpl salesService;

    @BeforeEach
    public void setUp() {
        salesService = new SalesServiceImpl();
    }

    @Test
    public void testGenerateReceipt() {
        String content = "1 book at 12.49\n1 music CD at 14.99\n1 chocolate bar at 0.85";
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", content.getBytes());

        String expectedReceipt = "1 book: 12.49\n1 music CD: 16.49\n1 chocolate bar: 0.85\nSales Taxes: 1.50 Total: 29.83\n";
        String receipt = salesService.generateReceipt(file);

        assertEquals(expectedReceipt, receipt);
    }

    @Test
    public void testCalculateTaxForExemptProduct() {
        Product product = new Product();
        product.setName("book");
        product.setPrice(12.49);
        product.setImported(false);
        product.setExempt(true);
        product.setNumber(1);

        double tax = salesService.calculateTax(product);
        assertEquals(0.0, tax);
    }

    @Test
    public void testCalculateTaxForNonExemptProduct() {
        Product product = new Product();
        product.setName("music CD");
        product.setPrice(14.99);
        product.setImported(false);
        product.setExempt(false);
        product.setNumber(1);

        double tax = salesService.calculateTax(product);
        assertEquals(1.50, tax, 0.01);
    }

    @Test
    public void testCalculateTaxForImportedProduct() {
        Product product = new Product();
        product.setName("imported chocolate");
        product.setPrice(10.00);
        product.setImported(true);
        product.setExempt(true);
        product.setNumber(1);

        double tax = salesService.calculateTax(product);
        assertEquals(0.50, tax, 0.01);
    }

    @Test
    public void testCalculateTaxForNonExemptImportedProduct() {
        Product product = new Product();
        product.setName("imported music CD");
        product.setPrice(14.99);
        product.setImported(true);
        product.setExempt(false);
        product.setNumber(1);

        double tax = salesService.calculateTax(product);
        assertEquals(2.25, tax, 0.01);
    }
}
