package com.xu.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

/**
 * Represents a user for the application
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserModel {
    @Id
    private String id;
    private String name;
    private String imageUrl;
    private String provider;
    private boolean twoFactorEnabled;
    private String totpSecret;
}
