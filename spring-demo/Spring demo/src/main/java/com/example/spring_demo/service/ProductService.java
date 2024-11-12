package com.example.spring_demo.service;

import com.example.spring_demo.controller.dto.ProductResponseDto;
import com.example.spring_demo.controller.dto.SaveProductDto;
import com.example.spring_demo.domain.Product;
import com.example.spring_demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    public List<ProductResponseDto> findAll() {
        return productRepository.findAll().stream()
                .map(product -> modelMapper.map(product, ProductResponseDto.class))
                .toList();
    }

    public Optional<ProductResponseDto> findById(Long id) {
        return productRepository.findById(id)
                .map(product -> modelMapper.map(product, ProductResponseDto.class));
    }

    public Optional<ProductResponseDto> update(Long id, SaveProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);
        product.setId(id);

        Optional<Product> foundProduct = productRepository.findById(id);
        if (foundProduct.isEmpty()) {
            return Optional.empty();
        }

        product = productRepository.save(product);
        return Optional.of(modelMapper.map(product, ProductResponseDto.class));
    }

    public ProductResponseDto save(SaveProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);
        product = productRepository.save(product);

        return modelMapper.map(product, ProductResponseDto.class);
    }

    public void deleteById(Long id) {
        this.productRepository.deleteById(id);
    }
}
