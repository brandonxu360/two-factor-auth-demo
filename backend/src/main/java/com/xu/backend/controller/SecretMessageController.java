package com.xu.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecretMessageController {

    @GetMapping("/secret-message")
    public String getSecretMessage() {
        return "The secret message is: 'Hello, World!'";
    }
}
