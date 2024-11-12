package com.example.spring_demo.integratieTests;

import com.example.spring_demo.controller.dto.SaveProductDto;
import com.example.spring_demo.domain.Product;
import com.example.spring_demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ProductControllerIT extends AbstractIntegrationTest {

    private final ProductRepository productRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void validate_that_all_result_out_of_database_are_returned() throws Exception {
        Product product1 = Product.builder().name("test_1").price(2.0).description("description_1").build();
        Product product2 = Product.builder().name("test_2").price(1.5).description("description_2").build();
        Product product3 = Product.builder().name("test_3").price(11.3).description("description_3").build();

        productRepository.saveAll(List.of(product1, product2, product3));

        mockMvc.perform(get("/product").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));

        productRepository.deleteAll();
    }

    @Test
    void validate_that_when_searching_on_existing_id_item_is_returend() throws Exception {
        Product product = Product.builder().name("test").price(2.0).description("description_1").build();

        Product savedProduct = productRepository.save(product);

        mockMvc.perform(get("/product/" + savedProduct.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.price").value(2.0))
                .andExpect(jsonPath("$.description").value("description_1"));

        productRepository.deleteAll();
    }

    @Test
    void validate_that_when_searching_on_non_existing_number_gives_not_found_http_response() throws Exception {
        mockMvc.perform(get("/product/9999").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void validate_that_when_searching_item_is_saved_and_correct_result_is_returned() throws Exception {
        SaveProductDto productDto = new SaveProductDto("test_name", 3.3, "description_test");
        String jsonQuery = objectMapper.writeValueAsString(productDto);

        var response = mockMvc.perform(post("/product").content(jsonQuery).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test_name"))
                .andExpect(jsonPath("$.price").value(3.3))
                .andExpect(jsonPath("$.description").value("description_test"))
                .andReturn();

        String stringResponse = response.getResponse().getContentAsString();
        Product savedProduct = objectMapper.readValue(stringResponse, Product.class);

        assertNotNull(savedProduct.getId());

        productRepository.deleteAll();
    }

    @Test
    void validate_that_when_updating_product_product_is_updated() throws Exception {
        // TODO test update.
    }

    @Test
    void validate_that_when_trying_to_update_non_existing_product_404_not_found_http_is_returned() throws Exception {
        SaveProductDto productDto = new SaveProductDto("test_name", 3.3, "description_test");
        String jsonQuery = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(put("/product/999").content(jsonQuery).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void validate_that_when_deleting_item_200_response_is_returned() throws Exception {
        Product product = Product.builder().name("test").price(2.0).description("description_1").build();
        Product savedProduct = productRepository.save(product);

        mockMvc.perform(get("/product/" + savedProduct.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        productRepository.deleteAll();
    }
}
