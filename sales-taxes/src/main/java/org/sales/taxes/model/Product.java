package org.sales.taxes.model;

import lombok.Data;

@Data
public class Product {

    private int number;
    private String name;
    private double price;
    private boolean isImported;
    private boolean isExempt;
}
