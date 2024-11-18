package com.xu.backend.model;

import lombok.Getter;

@Getter
public class UserDTO {
    private final String id;
    private final String name;
    private final String imageUrl;
    private final String provider;
    private final boolean twoFactorEnabled;

    public UserDTO(UserModel user) {
        this.id = user.getId();
        this.name = user.getName();
        this.imageUrl = user.getImageUrl();
        this.provider = user.getProvider();
        this.twoFactorEnabled = user.isTwoFactorEnabled();
    }
}
