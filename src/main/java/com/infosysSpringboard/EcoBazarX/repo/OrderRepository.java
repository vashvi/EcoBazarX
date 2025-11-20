package com.infosysSpringboard.EcoBazarX.repo;

import com.infosysSpringboard.EcoBazarX.model.Order;
import com.infosysSpringboard.EcoBazarX.model.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(Users user);

    List<Order> findByUserUsername(String username);

    @Query("SELECT DISTINCT o FROM Order o JOIN o.items i WHERE i.product.postedBy = :seller")
    List<Order> findOrdersBySeller(@Param("seller") Users seller);

    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o")
    double sumTotalPrice();

    @Modifying
    @Transactional
    @Query("DELETE FROM Order o WHERE o.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
