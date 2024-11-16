package com.xu.backend.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Model for OAuth user, DTO that will be converted to UserModel
 */
@Getter
@Setter
@AllArgsConstructor
public class OAuthUserModel {

    @Id
    private String id;
    private String name;
    private String imageUrl;
    private String provider;

}

