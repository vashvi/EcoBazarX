package com.infosysSpringboard.EcoBazarX.repo;

import com.infosysSpringboard.EcoBazarX.model.Products;
import com.infosysSpringboard.EcoBazarX.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Products, Long>, JpaSpecificationExecutor<Products> {
    List<Products> findByPostedBy(Users postedBy);

    @Query("""
        SELECT p.category, COUNT(p.id)
        FROM Products p
        GROUP BY p.category
    """)
    List<Object[]> getCategoryDistribution();

//    @Query("SELECT COUNT(DISTINCT p.seller.id) FROM OrderItem oi JOIN oi.product p WHERE oi.order.orderDate >= CURRENT_DATE - 30")
//    long countDistinctSellersWithSalesLast30Days();

//    @Query("""
//    SELECT COUNT(DISTINCT p.postedBy.id)
//    FROM OrderItem oi
//    JOIN oi.product p
//    WHERE oi.order.orderDate >= CURRENT_DATE - 30
//""")
//    long countDistinctSellersWithSalesLast30Days();


//    long countByStatus(String status);






}
