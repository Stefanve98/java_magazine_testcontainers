package com.example.spring_demo.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SaveProductDto {

    private String name;

    private double price;

    private String description;
}
