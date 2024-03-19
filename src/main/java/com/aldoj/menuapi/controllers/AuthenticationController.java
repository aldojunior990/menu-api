package com.aldoj.menuapi.controllers;

import com.aldoj.menuapi.configs.security.TokenService;
import com.aldoj.menuapi.domain.user.AuthenticationDTO;
import com.aldoj.menuapi.domain.user.LoginResponseDTO;
import com.aldoj.menuapi.domain.user.RegisterDTO;
import com.aldoj.menuapi.domain.user.User;
import com.aldoj.menuapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody AuthenticationDTO data) {
        var user = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(user);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTO data) {
        if (this.repository.findByEmail(data.email()) != null) return ResponseEntity.badRequest().build();
        String hashPassword = new BCryptPasswordEncoder().encode(data.password());
        User user = new User(data.user_name(), data.email(), hashPassword);
        this.repository.save(user);
        return ResponseEntity.ok().build();
    }


}
