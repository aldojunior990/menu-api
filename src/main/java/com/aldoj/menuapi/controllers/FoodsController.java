package com.aldoj.menuapi.controllers;

import com.aldoj.menuapi.configs.security.SecurityFilter;
import com.aldoj.menuapi.configs.security.TokenService;
import com.aldoj.menuapi.domain.food.FoodDTO;
import com.aldoj.menuapi.domain.food.Foods;
import com.aldoj.menuapi.domain.user.User;
import com.aldoj.menuapi.repositories.FoodsRepository;
import com.aldoj.menuapi.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("foods")
public class FoodsController {

    @Autowired
    private SecurityFilter securityFilter;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodsRepository foodsRepository;

    @PostMapping("/create")
    public ResponseEntity<FoodDTO> create(@RequestBody FoodDTO data, HttpServletRequest request) {
        var token = securityFilter.recoverToken(request);
        if (token != null) {
            var email = tokenService.validateToken(token);
            var user = userRepository.findByEmail(email);
            var food = new Foods(data, (User) user);
            foodsRepository.save(food);
            return ResponseEntity.ok(data);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/get")
    public ResponseEntity<List<FoodDTO>> getAll(HttpServletRequest request) {
        var token = securityFilter.recoverToken(request);
        if (token != null) {
            var email = tokenService.validateToken(token);
            var user = (User) userRepository.findByEmail(email);
            var foods = foodsRepository.findAllByUserId(user.getId()).stream().map(f -> new FoodDTO(f.getName(), f.getDescription(), f.getPrice()));
            return ResponseEntity.ok(foods.toList());
        }
        return ResponseEntity.badRequest().build();
    }
}
