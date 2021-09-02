package com.SaveMyRoaming.savemyroaming.repositories;

import com.SaveMyRoaming.savemyroaming.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);

    @Query("SELECT user FROM UserEntity user WHERE user.verificationCode = ?1")
    UserEntity findByVerificationCode(String verificationCode);
}
