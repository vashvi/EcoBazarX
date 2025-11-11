package com.infosysSpringboard.EcoBazarX.service;

import com.infosysSpringboard.EcoBazarX.model.Order;
import com.infosysSpringboard.EcoBazarX.model.Products;
import com.infosysSpringboard.EcoBazarX.model.Users;
import com.infosysSpringboard.EcoBazarX.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public Products saveProduct(Products product) {
        return productRepo.save(product);
    }

    public List<Products> findProductsBySeller(Users seller) {
        return productRepo.findByPostedBy(seller);
    }

    public Products updateProduct(Long productId, Products updatedProduct) {
        Products existing = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setCategory(updatedProduct.getCategory());
        existing.setPrice(updatedProduct.getPrice());
        existing.setStockQuantity(updatedProduct.getStockQuantity());
        existing.setImageUrl(updatedProduct.getImageUrl());
        existing.setCarbonFootprint(updatedProduct.getCarbonFootprint());
        return productRepo.save(existing);
    }

    public void deleteProduct(Long productId) {
        productRepo.deleteById(productId);
    }

    public List<Products> getAllProducts() {
        return productRepo.findAll();
    }


}
