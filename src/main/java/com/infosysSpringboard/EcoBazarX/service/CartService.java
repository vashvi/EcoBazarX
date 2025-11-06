package com.infosysSpringboard.EcoBazarX.service;

import com.infosysSpringboard.EcoBazarX.model.Cart;
import com.infosysSpringboard.EcoBazarX.model.CartItem;
import com.infosysSpringboard.EcoBazarX.model.Products;
import com.infosysSpringboard.EcoBazarX.model.Users;
import com.infosysSpringboard.EcoBazarX.repo.CartItemRepo;
import com.infosysSpringboard.EcoBazarX.repo.CartRepo;
import com.infosysSpringboard.EcoBazarX.repo.ProductRepo;
import com.infosysSpringboard.EcoBazarX.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartService {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    public Cart getOrCreateCart(Users user) {
        return cartRepo.findByUser(user).orElseGet(() -> {
            Cart newCart = Cart.builder().user(user).build();
            return cartRepo.save(newCart);
        });
    }

    public Cart addToCart(String username, Long productId, Integer quantity) {
        Users user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Products product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = getOrCreateCart(user);

        CartItem existingItem = cart.getItems().stream()
                .filter(i -> i.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.calculateTotals();
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();
            newItem.calculateTotals();
            cart.getItems().add(newItem);
        }

        recalculateCartTotals(cart);
        return cartRepo.save(cart);
    }

    public Cart removeFromCart(String username, Long productId) {
        Users user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = getOrCreateCart(user);
        cart.getItems().removeIf(item -> item.getProduct().getProductId().equals(productId));

        recalculateCartTotals(cart);
        return cartRepo.save(cart);
    }

    public Cart viewCart(String username) {
        Users user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return getOrCreateCart(user);
    }

    public void clearCart(String username) {
        Users user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = getOrCreateCart(user);
        cart.getItems().clear();
        cartRepo.save(cart);
    }

    private void recalculateCartTotals(Cart cart) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal totalCarbon = BigDecimal.ZERO;

        for (CartItem item : cart.getItems()) {
            totalPrice = totalPrice.add(item.getSubtotal());
            totalCarbon = totalCarbon.add(item.getCarbonTotal());
        }

        cart.setTotalPrice(totalPrice);
        cart.setTotalCarbon(totalCarbon);
    }


    public Cart updateItemQuantity(String username, Long productId, Integer quantity) {
        Users user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = getOrCreateCart(user);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found in cart"));

        if (quantity <= 0) {
            // If quantity is zero or negative → remove it
            cart.getItems().remove(item);
        } else {
            item.setQuantity(quantity);
            item.calculateTotals(); // Recalculate price + carbon footprint
        }

        recalculateCartTotals(cart);
        return cartRepo.save(cart);
    }

}
