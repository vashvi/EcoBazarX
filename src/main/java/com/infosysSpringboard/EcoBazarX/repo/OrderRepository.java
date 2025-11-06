package com.infosysSpringboard.EcoBazarX.repo;

import com.infosysSpringboard.EcoBazarX.model.Order;
import com.infosysSpringboard.EcoBazarX.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(Users user);

    List<Order> findByUserUsername(String username);
}
