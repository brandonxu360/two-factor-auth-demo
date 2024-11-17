package com.xu.backend.repository;

import com.xu.backend.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, String> {
    @Query("SELECT user.twoFactorEnabled FROM UserModel user WHERE user.id = ?1")
    Boolean findTwoFactorEnabledById(String id);
}
