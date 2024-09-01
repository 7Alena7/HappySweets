package com.alena.happysweets;

import com.alena.happysweets.model.Role;
import com.alena.happysweets.model.User;
import com.alena.happysweets.repository.UserRepository;
import com.alena.happysweets.service.UserDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserDetailServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailService userDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
//test checks if UserDetails is correctly loaded, verifying the username, password, and authorities
    @Test
    void loadUserByUsername_UserExists() {
        // Given
        Role role = new Role();
        role.setName("ROLE_USER");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRoles(Arrays.asList(role));

        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        verify(userRepository, times(1)).findUserByEmail("test@example.com");
    }
//test ensures that if a user is not found, a UsernameNotFoundException is thrown.
    @Test
    void loadUserByUsername_UserDoesNotExist() {
        when(userRepository.findUserByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailService.loadUserByUsername("nonexistent@example.com"));
        verify(userRepository, times(1)).findUserByEmail("nonexistent@example.com");
    }
}