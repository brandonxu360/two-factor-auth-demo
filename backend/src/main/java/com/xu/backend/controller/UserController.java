package com.xu.backend.controller;

import com.xu.backend.model.OAuthUserModel;
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
    public UserModel getUser(@AuthenticationPrincipal OAuth2User principal, OAuth2AuthenticationToken authToken) {
        String id = principal.getName();
        Map<String, Object> attributes = principal.getAttributes();
        String provider = authToken.getAuthorizedClientRegistrationId();

        return userService.getUserFromOAuth(id, provider, attributes);
    }
}