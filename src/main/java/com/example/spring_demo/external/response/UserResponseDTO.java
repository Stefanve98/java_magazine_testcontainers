package com.example.spring_demo.external.response;

public record UserResponseDTO(int id, String name, Role role, String description) {

    public enum Role {
        ADMIN,
        USER
    }
}
