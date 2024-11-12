package com.example.spring_demo.controller.dto;

import lombok.Data;

@Data
public class ProductResponseDto {

    private Long id;

    private String name;

    private double price;

    private String description;
}
