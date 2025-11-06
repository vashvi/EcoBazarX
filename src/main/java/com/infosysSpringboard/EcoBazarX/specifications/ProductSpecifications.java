package com.infosysSpringboard.EcoBazarX.specifications;

import com.infosysSpringboard.EcoBazarX.model.Products;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecifications {

    public static Specification<Products> hasName(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Products> hasCategory(String category) {
        return (root, query, cb) ->
                category == null ? null : cb.equal(cb.lower(root.get("category")), category.toLowerCase());
    }

    public static Specification<Products> priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, cb) -> {
            if (minPrice != null && maxPrice != null)
                return cb.between(root.get("price"), minPrice, maxPrice);
            else if (minPrice != null)
                return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
            else if (maxPrice != null)
                return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
            else
                return null;
        };
    }

    public static Specification<Products> carbonBelow(BigDecimal maxCarbon) {
        return (root, query, cb) ->
                maxCarbon == null ? null : cb.lessThanOrEqualTo(root.get("carbonFootprint"), maxCarbon);
    }
}
