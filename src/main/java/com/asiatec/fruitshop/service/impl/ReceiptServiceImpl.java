package com.asiatec.fruitshop.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.asiatec.fruitshop.dto.Receipt;
import com.asiatec.fruitshop.dto.ReceiptProduct;
import com.asiatec.fruitshop.service.ProductService;
import com.asiatec.fruitshop.service.ReceiptService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceiptServiceImpl implements ReceiptService{

    private Receipt receipt;
    
    @Autowired
    private ProductService productService;

    @Override
    public void createReceipt(List<ReceiptProduct> receiptProducts) {
        receipt = new Receipt();
        receipt.setProducts(receiptProducts);

        applyOffers();
    }

    @Override
    public int getReceiptProductIndex(String productName) {
        return receipt.getProducts().indexOf(receipt.getProducts().stream()
                .filter(x -> x.getName().equalsIgnoreCase(productName)).findFirst().get());
    }

    @Override
    public Receipt getReceipt() {
        return receipt;
    }

    private void applyOffers(){
        List<ReceiptProduct> products = new ArrayList<>();
        List<String> appliedOffers = new ArrayList<>();
        int extraOranges = 0;
        double specialDiscount = 0;
        double subTotalAmount = 0;
        for (ReceiptProduct rp : receipt.getProducts()) {
            double discount = 0;
            if (rp.getName().equalsIgnoreCase("apple") && rp.getQuantity() >= 3) {
                appliedOffers.add("Buy 3 Apples and pay 2.");
                discount = Math.floor(rp.getQuantity() / 3) * rp.getPrice();
                rp.setDiscount(discount);
                rp.setAmount(rp.getQuantity() * rp.getPrice() - rp.getDiscount());
            }

            if (rp.getName().equalsIgnoreCase("pear") && rp.getQuantity() >= 2) {
                appliedOffers.add("Get a free Orange for every 2 Pears you buy.");
                extraOranges = (int) Math.floor(rp.getQuantity() / 2);
                rp.setDiscount(discount);
                rp.setAmount(rp.getQuantity() * rp.getPrice() - rp.getDiscount());

                if (rp.getAmount() >= 4) {
                    appliedOffers.add("For every 4 € spent on Pears, we will deduct one \neuro from your final invoice.");
                    specialDiscount = Math.floor(rp.getAmount() / 4);
                }
            }

            subTotalAmount += rp.getAmount();

            products.add(rp);
        }

        if (extraOranges > 0) { // Extra Oranges
            ReceiptProduct rp;
            int index = getReceiptProductIndex("orange");
            // if(currentOranges < 1){ // No oranges in list
            if (index < 1) {
                rp = new ReceiptProduct(productService.findProduct("orange"));
                rp.setQuantity(extraOranges);
                rp.setDiscount(extraOranges * rp.getPrice());
                rp.setAmount(rp.getQuantity() * rp.getPrice() - rp.getDiscount());
                products.add(rp);
            } else {
                // int index = findReceiptProduct(receipt, "orange");
                rp = products.get(index);
                rp.setQuantity(rp.getQuantity() + extraOranges);
                rp.setDiscount(rp.getDiscount() + extraOranges * rp.getPrice());
                rp.setAmount(rp.getQuantity() * rp.getPrice() - rp.getDiscount());
                products.set(index, rp);
            }
            subTotalAmount += rp.getAmount();
        }

        receipt.setProducts(products);
        receipt.setOffersApplied(appliedOffers);
        receipt.setSpecialDiscount(specialDiscount);
        receipt.setSubTotalAmount(subTotalAmount);
        receipt.setTotalAmount(receipt.getSubTotalAmount() - receipt.getSpecialDiscount());
    }    
    
}
