package com.bcet.user_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcet.user_service.dto.ApiError;
import com.bcet.user_service.exception.UserAlreadyExistsException;
import com.bcet.user_service.exception.UserNotFoundException;
import com.bcet.user_service.model.UserData;
import com.bcet.user_service.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Create a new user
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserData user) {
        try {
            UserData createdUser = userService.createUser(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(new ApiError("User already exists: " + e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiError("An unexpected error occurred: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get All the users
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<UserData> allUsers = userService.getAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    // Get user by userId
    @GetMapping("/id/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable String userId) {
        try {
            UserData user = userService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(new ApiError("User not found with user id: " + userId),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiError("An unexpected error occurred: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            UserData user = userService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(new ApiError("User not found with email: " + email),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiError("An unexpected error occurred: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // Check if user exists by email
    @GetMapping("/email-exists")
    public ResponseEntity<Boolean> isExistingUser(@RequestParam String email) {
        Boolean isExistingUser = userService.isUserExistsByEmail(email);
        return ResponseEntity.ok(isExistingUser);
    }

    // TODO: Update user
    // @PutMapping("update/{userId}")
    // public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody
    // String entity) {}

    // Delete user
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {

        try {
            userService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body("User not found with id " + userId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred: " + e.getMessage());
        }
    }

}
