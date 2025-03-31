package org.sales.taxes.service.impl;

import org.sales.taxes.model.Product;
import org.sales.taxes.service.SalesService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalesServiceImpl implements SalesService {

    private static final String AT_VALUE = " at ";
    private static final String IMPORTED = "imported";
    private static final String BOOK = "book";
    private static final String CHOCOLATE = "chocolate";
    private static final String PILLS = "pills";
    private static final String SPACE = " ";
    private static final double BASIC_SALES_TAX_RATE = 0.1;
    private static final double IMPORT_DUTY_RATE = 0.05;

    @Override
    public String generateReceipt(final MultipartFile file) {
        List<Product> products = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(AT_VALUE);
                String[] details = parts[0].split(SPACE, 2);
                int quantity = Integer.parseInt(details[0]);
                String name = details[1];
                double price = Double.parseDouble(parts[1]);
                boolean isImported = name.contains(IMPORTED);
                boolean isExempt = name.contains(BOOK) || name.contains(CHOCOLATE) || name.contains(PILLS);

                for (int i = 0; i < quantity; i++) {
                    Product product = new Product();
                    product.setNumber(quantity);
                    product.setName(name);
                    product.setPrice(price);
                    product.setImported(isImported);
                    product.setExempt(isExempt);
                    products.add(product);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        StringBuilder receiptOutput = getReceiptOutput(products);
        return receiptOutput.toString();
    }

    private StringBuilder getReceiptOutput(List<Product> products) {
        double totalCost = 0.0;
        double totalSalesTax = 0.0;
        StringBuilder receiptOutput = new StringBuilder();

        for (Product product : products) {
            double tax = calculateTax(product);
            double priceWithTax = product.getPrice() + tax;
            product.setPrice(priceWithTax);
            totalCost += priceWithTax;
            totalSalesTax += tax;
            receiptOutput.append(String.format("%s %s: %.2f\n", product.getNumber(), product.getName(), priceWithTax).replace(",", "."));
        }

        receiptOutput.append(String.format("Sales Taxes: %.2f ", totalSalesTax).replace(",", "."));
        receiptOutput.append(String.format("Total: %.2f\n", totalCost).replace(",", "."));
        return receiptOutput;
    }

    @Override
    public double calculateTax(final Product product) {
        double tax = 0.0;
        if (!product.isExempt()) {
            tax += product.getPrice() * BASIC_SALES_TAX_RATE;
        }
        if (product.isImported()) {
            tax += product.getPrice() * IMPORT_DUTY_RATE;
        }
        return roundUpToNearestFiveCents(tax);
    }

    private double roundUpToNearestFiveCents(double amount) {
        return BigDecimal.valueOf(Math.ceil(amount * 20) / 20).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
