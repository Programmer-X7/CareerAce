package com.bcet.user_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcet.user_service.model.UserData;

@Repository
public interface UserRepository extends JpaRepository<UserData, String> {

    boolean existsByEmail(String email);

    Optional<UserData> findByEmail(String email);

    long count();

}
