package com.infosysSpringboard.EcoBazarX.repo;

import com.infosysSpringboard.EcoBazarX.model.Products;
import com.infosysSpringboard.EcoBazarX.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Products, Long>, JpaSpecificationExecutor<Products> {
    List<Products> findByPostedBy(Users postedBy);
}
