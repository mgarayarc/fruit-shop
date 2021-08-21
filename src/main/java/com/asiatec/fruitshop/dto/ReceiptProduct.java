package com.asiatec.fruitshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReceiptProduct extends Product{

    private int quantity;
    private double discount;
    private double amount;

    public ReceiptProduct(Product product){
        super(product.getName(), product.getPrice());
    }
    
}
