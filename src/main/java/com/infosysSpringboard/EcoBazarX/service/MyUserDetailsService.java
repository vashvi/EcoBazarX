package com.infosysSpringboard.EcoBazarX.service;

import com.infosysSpringboard.EcoBazarX.model.UserPrincipal;
import com.infosysSpringboard.EcoBazarX.model.Users;
import com.infosysSpringboard.EcoBazarX.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{

        Optional<Users> user = repo.findByUsername(username);

        if(user.isEmpty()) {
            System.out.println("User Not Found");
            throw new UsernameNotFoundException("User not found");
        }
        return new UserPrincipal(user.orElse(null));
    }
}
