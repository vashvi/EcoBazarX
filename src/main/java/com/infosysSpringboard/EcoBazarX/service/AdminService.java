package com.infosysSpringboard.EcoBazarX.service;

import com.infosysSpringboard.EcoBazarX.model.Role;
import com.infosysSpringboard.EcoBazarX.model.Users;
import com.infosysSpringboard.EcoBazarX.repo.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final UserRepo userRepo;

    public AdminService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<Users> findAllUsers() {
        return userRepo.findAll();
    }

    public List<Users> findUsersByRole(Role role) {
        return userRepo.findByRole(role);
    }

    public Users changeUserRole(String username, Role newRole) {
        Users user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        user.setRole(newRole);
        return userRepo.save(user);
    }

    public void deleteUser(Long userId) {
        userRepo.deleteById(userId);
    }

    public Users updateUserDetails(Long userId, Users updatedUser) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEmail(updatedUser.getEmail());
        user.setPhone(updatedUser.getPhone());
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        return userRepo.save(user);
    }

}
