package com.alena.happysweets.service;

import com.alena.happysweets.model.UserDetail;
import com.alena.happysweets.model.User;
import com.alena.happysweets.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
//Implementation of all the UserRepository Methods
@Service
public class UserDetailService implements UserDetailsService {
    UserRepository userRepository;
    @Autowired
    public UserDetailService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByEmail(email);
        user.orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.map(UserDetail::new).get();
    }
}
