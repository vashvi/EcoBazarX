package com.infosysSpringboard.EcoBazarX.repo;

import com.infosysSpringboard.EcoBazarX.model.Order;
import com.infosysSpringboard.EcoBazarX.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(Users user);

    List<Order> findByUserUsername(String username);

    @Query("SELECT DISTINCT o FROM Order o JOIN o.items i WHERE i.product.postedBy = :seller")
    List<Order> findOrdersBySeller(@Param("seller") Users seller);


}
