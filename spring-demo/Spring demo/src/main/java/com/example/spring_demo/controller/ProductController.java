package com.example.spring_demo.controller;

import com.example.spring_demo.controller.dto.ProductResponseDto;
import com.example.spring_demo.controller.dto.SaveProductDto;
import com.example.spring_demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/product")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductResponseDto> getAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ProductResponseDto getProduct(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PutMapping("/{id}")
    public ProductResponseDto update(@PathVariable Long id, @RequestBody SaveProductDto saveProduct) {
        return productService.update(id, saveProduct);
    }

    @PostMapping
    public ProductResponseDto save(@RequestBody SaveProductDto product) {
        return productService.save(product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.deleteById(id);
    }
}
