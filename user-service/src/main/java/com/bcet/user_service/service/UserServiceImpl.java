package com.bcet.user_service.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bcet.user_service.exception.UserAlreadyExistsException;
import com.bcet.user_service.exception.UserNotFoundException;
import com.bcet.user_service.model.UserData;
import com.bcet.user_service.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserData createUser(UserData user) {

        // check if user already exists by email
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }

        // set userId if not assigned
        if (user.getUserId() == null) {
            user.setUserId(UUID.randomUUID().toString());
        }

        return userRepository.save(user);
    }

    @Override
    public List<UserData> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserData getUserById(String userId) {
        Optional<UserData> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        return user.get();
    }

    @Override
    public UserData getUserByEmail(String email) {
        Optional<UserData> user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            throw new UserNotFoundException("User not found with email: " + email);
        }
        return user.get();
    }

    @Override
    public boolean isUserExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // @Override
    // public ResponseEntity<?> updateUser() {}

    @Override
    public void deleteUser(String userId) {
        Optional<UserData> existingUser = userRepository.findById(userId);
        if (!existingUser.isPresent()) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
    }

}
