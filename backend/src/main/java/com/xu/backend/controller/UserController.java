package com.xu.backend.controller;

import com.xu.backend.model.OAuthUserModel;
import com.xu.backend.model.UserDTO;
import com.xu.backend.model.UserModel;
import com.xu.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    final private UserService userService;

    // Get the user information
    @GetMapping("/user")
    public UserDTO getUser(@AuthenticationPrincipal OAuth2User principal, OAuth2AuthenticationToken authToken) {
        if (principal == null) {
            return null;
        }

        String id = principal.getName();
        Map<String, Object> attributes = principal.getAttributes();
        String provider = authToken.getAuthorizedClientRegistrationId();

        UserModel user = userService.getUserFromOAuth(id, provider, attributes);
        return new UserDTO(user);
    }
}