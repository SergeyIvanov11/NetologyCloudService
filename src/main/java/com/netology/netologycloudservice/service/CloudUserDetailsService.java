package com.netology.netologycloudservice.service;

import com.netology.netologycloudservice.exception.UserNotFoundException;
import com.netology.netologycloudservice.repository.CloudUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CloudUserDetailsService implements UserDetailsService {
    private final CloudUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }
}
