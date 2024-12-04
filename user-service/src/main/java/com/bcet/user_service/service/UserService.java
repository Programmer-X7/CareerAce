package com.bcet.user_service.service;

import java.util.List;

import com.bcet.user_service.model.UserData;

public interface UserService {

    UserData createUser(UserData user);

    List<UserData> getAllUsers();

    UserData getUserById(String userId);

    UserData getUserByEmail(String email);

    boolean isUserExistsByEmail(String email);

    // ResponseEntity<?> updateUser();

    void deleteUser(String userId);

}
