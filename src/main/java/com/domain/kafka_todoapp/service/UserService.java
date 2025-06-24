package com.domain.kafka_todoapp.service;

import com.domain.kafka_todoapp.db.user.User;
import com.domain.kafka_todoapp.db.user.UserRepository;
import com.domain.kafka_todoapp.dto.UserRequestDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getCurrentUser() throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Access denided");
        }
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }

    @Transactional
    public User updateUser(UserRequestDTO dto) throws AccessDeniedException {
        User updatedUser = getCurrentUser();

        updatedUser.setUsername(dto.getUsername());
        updatedUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        updatedUser.setEmail(dto.getEmail());
        updatedUser.setAge(dto.getAge());

        return userRepository.save(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) throws AccessDeniedException {
        User userToDelete = getCurrentUser();

        if (userToDelete.getId().equals(id)) {
            userRepository.deleteById(id);
        } else {
            throw new AccessDeniedException("You can't delete another user.");
        }
    }
}
