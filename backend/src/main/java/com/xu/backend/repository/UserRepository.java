package com.xu.backend.repository;

import com.xu.backend.model.UserModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, String> {
    // Get the two-factor authentication status for the user with the given id.
    @Query("SELECT user.twoFactorEnabled FROM UserModel user WHERE user.id = ?1")
    Boolean findTwoFactorEnabledById(String id);

    // Save the TOTP secret for the user with the given id.
    @Modifying
    @Transactional
    @Query("UPDATE UserModel user SET user.totpSecret = :totpSecret WHERE user.id = :id")
    void saveTotpSecretById(@Param("id") String id, @Param("totpSecret") String totpSecret);


    // Update the two-factor authentication status for the user with the given id.
    @Modifying
    @Transactional
    @Query("UPDATE UserModel u SET u.twoFactorEnabled = :status WHERE u.id = :id")
    void updateTwoFactorStatus(@Param("id") String id, @Param("status") boolean status);

    // Delete the TOTP secret for the user with the given id.
    @Modifying
    @Transactional
    @Query("UPDATE UserModel u SET u.totpSecret = null WHERE u.id = :id")
    void deleteTotpSecretById(@Param("id") String id);
}
