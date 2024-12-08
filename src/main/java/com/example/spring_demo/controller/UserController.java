package com.example.spring_demo.controller;


import com.example.spring_demo.external.UserService;
import com.example.spring_demo.external.response.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserResponseDTO getAll(@PathVariable int id) {
        return userService.getUser(id);
    }
}
