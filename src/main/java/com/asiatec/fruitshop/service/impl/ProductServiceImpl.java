package com.asiatec.fruitshop.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.asiatec.fruitshop.dto.Product;
import com.asiatec.fruitshop.service.ProductService;

import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private List<Product> products;

    @Override
    public Product findProduct(String productName) {
        return products.stream().filter(x -> x.getName().equalsIgnoreCase(productName)).findFirst().get();
    }

    @Override
    public List<Product> getProducts() {        
        return products;
    }

    @Override
    public void addProduct(Product product) {
        if(products == null || products.size() < 1) products = new ArrayList<>();
        products.add(product);
    }

}
