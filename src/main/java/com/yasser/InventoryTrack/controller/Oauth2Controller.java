package com.yasser.InventoryTrack.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
public class Oauth2Controller {

    @PostMapping("/token")
    public ResponseEntity<String> getToken(@RequestBody String body) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, String> map = mapper.readValue(body, HashMap.class);

        RestClient restClient = RestClient.builder().build();

        MultiValueMap<String, Object> keycloakBody = new LinkedMultiValueMap<>();

        keycloakBody.add("code", map.get("code"));
        keycloakBody.add("redirect_uri", "http://localhost:3000/auth/callback");
        keycloakBody.add("client_id", "InventoryManagment");
        keycloakBody.add("client_secret", "qsnM2Nj6A3FZ85hTTG2gozr7RrjoLRpX");
        keycloakBody.add("grant_type", "authorization_code");

        String response = "";

        try {
            response = restClient.post()
                    .uri("http://localhost:8080/realms/InventoryManagment/protocol/openid-connect/token")
                    .body(keycloakBody)
                    .retrieve()
                    .body(String.class);

        } catch (HttpClientErrorException e) {

            return ResponseEntity
                    .status(e.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(e.getResponseBodyAsString());
        };


        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(response);


    }
}
