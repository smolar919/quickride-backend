package com.quickride.demo.carrental.controller;

import com.quickride.demo.carrental.model.AppUser;
import com.quickride.demo.carrental.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public AppUser register(@RequestBody AppUser appUser) {
        return userService.registerUser(appUser);
    }

    @GetMapping("getByEmail/{email}")
    public AppUser getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }

    @GetMapping("/{id}")
    public AppUser getUserById(@PathVariable String id) {
        return userService.findById(id);
    }
}
