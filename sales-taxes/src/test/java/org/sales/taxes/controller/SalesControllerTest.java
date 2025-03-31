package org.sales.taxes.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sales.taxes.service.SalesService;
import org.springframework.mock.web.MockMultipartFile;

public class SalesControllerTest {

    @InjectMocks
    private SalesController salesController;

    @Mock
    private SalesService salesService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerateReceipt() {
        String content = "1 book at 12.49\n1 music CD at 14.99\n1 chocolate bar at 0.85";
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", content.getBytes());
        String expectedReceipt = "book: 12.49\nmusic CD: 16.49\nchocolate bar: 0.85\nSales Taxes: 1.50 Total: 29.83\n";
        when(salesService.generateReceipt(file)).thenReturn(expectedReceipt);
        String receipt = salesController.generateReceipt(file).getBody();
        assertEquals(expectedReceipt, receipt);
    }


    @Test
    public void testGenerateReceiptForInput2() {
        String content = "1 imported box of chocolates at 10.00\n1 imported bottle of perfume at 47.50";
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", content.getBytes());
        String expectedReceipt = "1 imported box of chocolates: 10.50\n1 imported bottle of perfume: 54.65\nSales Taxes: 7.65 Total: 65.15\n";
        when(salesService.generateReceipt(file)).thenReturn(expectedReceipt);
        String receipt = salesController.generateReceipt(file).getBody();
        assertEquals(expectedReceipt, receipt);
    }
    @Test
    public void testGenerateReceiptForInput3() {
        String content = "1 imported bottle of perfume at 27.99\n1 bottle of perfume at 18.99\n1 packet of headache pills at 9.75\n1 box of imported chocolates at 11.25";
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", content.getBytes());
        String expectedReceipt = "1 imported bottle of perfume: 32.19\n1 bottle of perfume: 20.89\n1 packet of headache pills: 9.75\n1 box of imported chocolates: 11.85\nSales Taxes: 6.70 Total: 74.68\n";
        when(salesService.generateReceipt(file)).thenReturn(expectedReceipt);
        String receipt = salesController.generateReceipt(file).getBody();
        assertEquals(expectedReceipt, receipt);
    }
}
