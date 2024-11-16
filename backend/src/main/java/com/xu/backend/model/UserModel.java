package com.xu.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserModel {
    private String id;
    private String name;
    private String imageUrl;
    private String provider;
}
