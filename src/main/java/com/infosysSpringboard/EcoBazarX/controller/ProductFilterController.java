package com.infosysSpringboard.EcoBazarX.controller;

import com.infosysSpringboard.EcoBazarX.model.Products;
import com.infosysSpringboard.EcoBazarX.service.ProductFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
public class ProductFilterController {

    @Autowired
    private ProductFilterService filterService;

    @GetMapping
    public ResponseEntity<List<Products>> getFilteredProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) BigDecimal maxCarbon
    ) {
        List<Products> results = filterService.filterProducts(name, category, minPrice, maxPrice, maxCarbon);
        return ResponseEntity.ok(results);
    }
}
