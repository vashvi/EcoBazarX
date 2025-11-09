package com.infosysSpringboard.EcoBazarX.controller;

import com.infosysSpringboard.EcoBazarX.model.Order;
import com.infosysSpringboard.EcoBazarX.model.Role;
import com.infosysSpringboard.EcoBazarX.model.Users;
import com.infosysSpringboard.EcoBazarX.service.AdminService;
import com.infosysSpringboard.EcoBazarX.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService service;

    @Autowired
    CheckoutService checkoutService;

    @GetMapping("/all-users")
    public ResponseEntity<List<Users>> findAllUsers(){
        List<Users> users = service.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/all-sellers")
    public ResponseEntity<List<Users>> findAllSellers() {
        return ResponseEntity.ok(service.findUsersByRole(Role.SELLER));
    }

    @GetMapping("/all-customers")
    public ResponseEntity<List<Users>> findAllCustomers() {
        return ResponseEntity.ok(service.findUsersByRole(Role.USER));
    }

    @GetMapping("/all-admins")
    public ResponseEntity<List<Users>> findAllAdmins() {
        return ResponseEntity.ok(service.findUsersByRole(Role.ADMIN));
    }
    @PutMapping("/change-role/{username}")
    public ResponseEntity<String> changeUserRole(
            @PathVariable String username,
            @RequestParam Role role) {

        service.changeUserRole(username, role);
        return ResponseEntity.ok("Role for user '" + username + "' updated to: " + role);
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully (ID: " + id + ")");
    }

    @PutMapping("/update-user/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Long id, @RequestBody Users updatedUser) {
        return ResponseEntity.ok(service.updateUserDetails(id, updatedUser));
    }

    @GetMapping("/all-orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(checkoutService.getAllOrders());
    }


}
