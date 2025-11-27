package com.ibrahim.dubaiconciergerie.demo.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "BearerAuth")
public class TestSecureController {

    @GetMapping("/api/secure/test")
    public String test() {
        return "OK, token valide !";
    }
}

