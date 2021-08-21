package com.asiatec.fruitshop.service;

import java.util.List;

import com.asiatec.fruitshop.dto.Product;

public interface ProductService {

    public Product findProduct(String productName);
    public List<Product> getProducts();
    public void addProduct(Product product);
    
}
