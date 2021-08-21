package com.asiatec.fruitshop.service;

import java.util.List;

import com.asiatec.fruitshop.dto.Receipt;
import com.asiatec.fruitshop.dto.ReceiptProduct;

public interface ReceiptService {

    public void createReceipt(List<ReceiptProduct> receiptProducts);
    public int getReceiptProductIndex(String productName);
    public Receipt getReceipt();
}
