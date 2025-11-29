package com.example.rbpo_aeroport.controllers;

import com.example.rbpo_aeroport.entities.ApplicationUser;
import com.example.rbpo_aeroport.services.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SecurityController {
    @Autowired
    private UserDetailsServiceImpl applicationUserService;

    @GetMapping("/register")
    public ResponseEntity<Void> registration(Model model) {
        model.addAttribute("applicationUser", new ApplicationUser());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<@Valid ApplicationUser> register(@ModelAttribute("applicationUser") @Valid ApplicationUser applicationUser, BindingResult bindingResult) {
        try {
            applicationUserService.fromApplicationUser(applicationUser);
            return ResponseEntity.ok(applicationUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("csrf-token")
    public Map<String, String> csrf(CsrfToken token){
        return Map.of("token",token.getToken());
    }
}
