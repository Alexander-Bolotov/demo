package com.example.demo.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController()
public class restController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/manage")
    JsonNode manage() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("MyHeader", "info/json");
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("name", "Ivan");
        jsonBody.put("surname", "Иванов");

        HttpEntity<String> request = new HttpEntity<>(jsonBody.toString(), headers);
        String result = restTemplate.postForObject("http://localhost:8080/tech", request, String.class);
        return objectMapper.readTree(result);
    }

    @PostMapping("/tech")
    ResponseEntity<?> tech(@RequestHeader Map<String, String> headersMap, @RequestBody String bodyString) throws JsonProcessingException {

        headersMap.put("ADDITIONAL HEADER", "MY HEADER IN /TEACH");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode bodyJson = mapper.readTree(bodyString);
        ((ObjectNode) bodyJson).put("!AGE!", "26");

        Map<Map<String, Map<String, String>>, Map<String, JsonNode>> response = new HashMap<>();
        Map<String, Map<String, String>> heads = new HashMap<>();
        Map<String, JsonNode> bodies = new HashMap<>();
        heads.put("HEADERS", headersMap);
        bodies.put("BODY", bodyJson);
        response.put(heads, bodies);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
