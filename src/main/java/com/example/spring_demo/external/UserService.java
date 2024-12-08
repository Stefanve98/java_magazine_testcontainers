package com.example.spring_demo.external;

import com.example.spring_demo.controller.exceptions.NonExistingUser;
import com.example.spring_demo.external.response.UserResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class UserService {

    @Value("${user-application.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public UserResponseDTO getUser(int id) {
        return handleGetRequest(id);
    }

    private UserResponseDTO handleGetRequest(int id) {
        ResponseEntity<UserResponseDTO> response;
        try {
            response = restTemplate.getForEntity(baseUrl + "/user/" + id, UserResponseDTO.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new NonExistingUser();
            }
            throw e;
        }

        return response.getBody();
    }
}
