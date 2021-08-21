package com.asiatec.fruitshop.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.asiatec.fruitshop.dto.Product;
import com.asiatec.fruitshop.dto.Receipt;
import com.asiatec.fruitshop.dto.ReceiptProduct;
import com.asiatec.fruitshop.service.ProductService;
import com.asiatec.fruitshop.service.ReceiptService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ShoppingController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ReceiptService receiptService;

    public void interactiveConsole() {        

        String[] filePaths = new String[2];

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Enter the product file path : ");
            filePaths[0] = reader.readLine();

            System.out.print("Enter the shopping list file path : ");
            filePaths[1] = reader.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }

        preLoadedConsole(filePaths);
    }

    public void preLoadedConsole(String[] filePaths) {
        for (String path : filePaths) {
            try (Scanner scanner = new Scanner(new File(path))) {
                List<String> lines = new ArrayList<>();
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    lines.add(line);
                }
                loadFile(lines);
            } catch (FileNotFoundException fnfe) {
                fnfe.printStackTrace();
            }
        }
        printReceipt();
    }

    private void loadFile(List<String> lines) {
        if (lines == null || lines.isEmpty() || lines.size() < 2) {
            System.out.println("One of the files is invalid.");
            return;
        }
        if (!lines.get(0).toLowerCase().contains("price") && !lines.get(0).toLowerCase().contains("quantity")) {
            System.out.println("One of the files is invalid.");
            return;
        }

        if (lines.get(0).toLowerCase().contains("price")) {
            lines.remove(0);
            for (String l : lines) {
                String[] productData = l.split(",");
                Product product = new Product(productData[0].trim().toUpperCase(),
                        Double.parseDouble(productData[1].trim()));
                productService.addProduct(product);
            }
        }

        if (lines.get(0).toLowerCase().contains("quantity") && productService.getProducts().size() > 0) {
            lines.remove(0);
            List<ReceiptProduct> receiptProducts = new ArrayList<>();
            for (String l : lines) {
                String[] productData = l.split(",");
                ReceiptProduct product = new ReceiptProduct(productService.findProduct(productData[0].trim().toUpperCase()));
                product.setQuantity(Integer.parseInt(productData[1].trim()));
                receiptProducts.add(product);
            }
            receiptService.createReceipt(receiptProducts);
        }
    }

    private void printReceipt(){
        Receipt receipt = receiptService.getReceipt();

        // Expected Receipt
        System.out.println("");
        System.out.println("==================================================");
        System.out.println("                     INVOICE                      ");
        System.out.println("==================================================");
        System.out.println("Product             Qty       Discount      Amount");
        for(ReceiptProduct rp : receipt.getProducts()){
            System.out.print(String.format("%-20s", rp.getName()));
            System.out.print(String.format("%-10s", rp.getQuantity()));
            System.out.print(String.format("%-10s", String.format("%.2f", rp.getDiscount())));
            System.out.println(String.format("%10s", String.format("%.2f", rp.getAmount())));
        }
        System.out.println("==================================================");
        System.out.println(String.format("%-25s", "SubTotal") + String.format("%25s", receipt.getSubTotalAmount()));
        System.out.println(String.format("%-25s", "Additional discount") + String.format("%25s", receipt.getSpecialDiscount()));
        System.out.println(String.format("%-25s", "Total") + String.format("%25s", receipt.getTotalAmount()));
        System.out.println("==================================================");
        System.out.println("Offers Applied;");
        for(String o : receipt.getOffersApplied()){            
            System.out.println("** "+o);
        }
        System.out.println("==================================================");
    }

}
