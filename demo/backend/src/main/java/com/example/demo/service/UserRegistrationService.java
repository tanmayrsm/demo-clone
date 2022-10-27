package com.example.demo.service;

import com.example.demo.dto.UserInfoDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    @Value("${auth0.userinfoEndpoint}")
    private String userInfoEndpoint;

    private final UserRepository userRepository;

    public String registerUser(String tokenValue){
        // call to user info endpoint
        HttpRequest httpRequest =  HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(userInfoEndpoint))
                .setHeader("Authorization", String.format("Bearer %s", tokenValue))
                .build();
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        try {
            // fetch user details
            HttpResponse<String> responseString =  httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String body = responseString.body();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            UserInfoDTO userInfoDTO = objectMapper.readValue(body, UserInfoDTO.class);

            // check if user in db
            Optional<User> userBySubject = userRepository.findBySub(userInfoDTO.getSub());
            if(userBySubject.isPresent()) {
                return userBySubject.get().getId();
            }

            // save user in db
            User user = new User();
            user.setFirstName(userInfoDTO.getFamilyName());
            user.setFullName(userInfoDTO.getFamilyName());
            user.setEmailAddress(userInfoDTO.getEmail());
            user.setSub(userInfoDTO.getSub());
            return userRepository.save(user).getId();
        } catch (Exception e) {
            throw  new RuntimeException("Error while registering user ::" + e.getMessage());
        }
    }
}
