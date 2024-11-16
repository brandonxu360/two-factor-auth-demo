package com.xu.backend.service;

import com.xu.backend.model.OAuthUserModel;
import com.xu.backend.model.UserModel;
import com.xu.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    final private UserRepository userRepository;

    // Map the OAuth attributes (potentially from different providers) to standardized OAuthUserModel object
    private OAuthUserModel mapToOAuthUser(String provider, Map<String, Object> attributes) {
        return switch (provider) {
            case "google" -> new OAuthUserModel(
                    (String) attributes.get("sub"),
                    (String) attributes.get("name"),
                    (String) attributes.get("picture"),
                    "google"
            );
            case "github" -> new OAuthUserModel(
                    String.valueOf(attributes.get("id")),
                    (String) attributes.get("login"),
                    (String) attributes.get("avatar_url"),
                    "github"
            );
            // Add more cases for other providers as needed
            default -> throw new IllegalArgumentException("Unsupported OAuth provider");
        };
    }

    /**
     * Retrieve the user from the database or create a new user from the OAuth info if it does not exist
     * @param id The user ID
     * @param provider The OAuth provider
     * @param attributes The user attributes from the OAuth provider
     * @return The user model representing the user in the application
     */
    public UserModel getUserFromOAuth(String id, String provider, Map<String, Object> attributes) {
        // Attempt to find the user in the database
        UserModel user = userRepository.findById(id).orElse(null);

        // If the user does not exist, create a new user from the OAuth info
        if (user == null) {
            // Map the OAuth attributes to a standardized OAuthUserModel object
            OAuthUserModel oauthUser = mapToOAuthUser(provider, attributes);

            // Create a new user model from the standardized OAuth info
            user = new UserModel(
                    oauthUser.getId(),
                    oauthUser.getName(),
                    oauthUser.getImageUrl(),
                    oauthUser.getProvider(),
                    false
            );
            userRepository.save(user);
        }

        return user;
    }

}
