package com.infosysSpringboard.EcoBazarX.repo;

import com.infosysSpringboard.EcoBazarX.model.Cart;
import com.infosysSpringboard.EcoBazarX.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(Users user);
}
