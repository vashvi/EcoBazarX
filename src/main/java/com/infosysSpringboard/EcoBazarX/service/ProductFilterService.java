package com.infosysSpringboard.EcoBazarX.service;


import com.infosysSpringboard.EcoBazarX.model.Products;
import com.infosysSpringboard.EcoBazarX.repo.ProductRepo;
import com.infosysSpringboard.EcoBazarX.specifications.ProductSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductFilterService {

    @Autowired
    private ProductRepo productRepo;

    public List<Products> filterProducts(String name, String category, BigDecimal minPrice, BigDecimal maxPrice, BigDecimal maxCarbon) {

        Specification<Products> spec = Specification.where(ProductSpecifications.hasName(name))
                .and(ProductSpecifications.hasCategory(category))
                .and(ProductSpecifications.priceBetween(minPrice, maxPrice))
                .and(ProductSpecifications.carbonBelow(maxCarbon));

        return productRepo.findAll(spec);
    }
}
