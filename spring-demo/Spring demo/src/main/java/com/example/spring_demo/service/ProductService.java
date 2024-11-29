package com.example.spring_demo.service;

import com.example.spring_demo.controller.dto.ProductResponseDto;
import com.example.spring_demo.controller.dto.SaveProductDto;
import com.example.spring_demo.controller.exceptions.NonExistingProduct;
import com.example.spring_demo.domain.Product;
import com.example.spring_demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public ProductResponseDto findById(Long id) {
        return productRepository.findById(id)
                .map(product -> modelMapper.map(product, ProductResponseDto.class))
                .orElseThrow(NonExistingProduct::new);
    }

    public ProductResponseDto update(Long id, SaveProductDto productDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(NonExistingProduct::new);

        Product updatingProduct = modelMapper.map(productDto, Product.class);
        updatingProduct.setId(product.getId());
        updatingProduct = productRepository.save(updatingProduct);

        return modelMapper.map(updatingProduct, ProductResponseDto.class);
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
