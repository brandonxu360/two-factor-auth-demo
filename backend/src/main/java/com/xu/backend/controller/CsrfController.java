package com.xu.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for CSRF requests
 */
@RestController
public class CsrfController {

    @GetMapping("/csrf")
    public void getCsrf() {
    }
}
