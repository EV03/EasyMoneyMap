package com.example.easymoneymapapi.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class TestUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String extractTokenFromResponse(String response) throws Exception {
        Map<String, String> map = objectMapper.readValue(response, new TypeReference<Map<String, String>>() {});
        return map.get("token");
    }
}