package com.xu.backend.controller;

import com.xu.backend.model.UserModel;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {

    @GetMapping("/user")
    public UserModel getUser(@AuthenticationPrincipal OAuth2User principal, OAuth2AuthenticationToken authToken) {
        Map<String, Object> attributes = principal.getAttributes();
        String provider = authToken.getAuthorizedClientRegistrationId();

        return mapToStandardUser(provider, attributes);
    }

    // Map the attributes to a standard UserModel object
    private UserModel mapToStandardUser(String provider, Map<String, Object> attributes) {
        return switch (provider) {
            case "google" -> new UserModel(
                    (String) attributes.get("sub"),
                    (String) attributes.get("name"),
                    (String) attributes.get("picture"),
                    "google"
            );
            case "github" -> new UserModel(
                    String.valueOf(attributes.get("id")),
                    (String) attributes.get("login"),
                    (String) attributes.get("avatar_url"),
                    "github"
            );
            // Add more cases for other providers as needed
            default -> throw new IllegalArgumentException("Unsupported OAuth provider");
        };
    }
}