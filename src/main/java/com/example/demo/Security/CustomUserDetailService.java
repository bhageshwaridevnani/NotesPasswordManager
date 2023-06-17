package com.example.demo.Security;

import com.example.demo.Model.EntityUser;
import com.example.demo.Repositroy.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       EntityUser user = userRepository.findByUserName(username);
       return new org.springframework.security.core.userdetails.User(user.getFirstName(), user.getPassword(), new ArrayList<>());
    }

    public EntityUser loadEntityEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found with email : " + email)
        );
    }
}
