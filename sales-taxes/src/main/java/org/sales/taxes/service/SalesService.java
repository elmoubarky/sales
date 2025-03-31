package org.sales.taxes.service;

import org.sales.taxes.model.Product;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

@Validated
public interface SalesService {

     /**
      * Generates a receipt from the given input file.
      *
      * @param file the input file containing product details
      * @return the formatted receipt string
      */
     String generateReceipt(final MultipartFile file);


     /**
      * Calculates the tax for the given product.
      *
      * @param product the product for which to calculate the tax
      * @return the tax amount
      */
     double calculateTax(Product product);
}
