package com.asiatec.fruitshop.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Receipt {

    private double totalAmount;
    private double subTotalAmount;
    private double specialDiscount;
    private List<ReceiptProduct> products;
    private List<String> offersApplied;   
    
}
