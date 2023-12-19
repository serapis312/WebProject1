package com.project.childprj.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {
    // ObjectMapper는 생성 비용이 비싸기 때문에 static으로 1회 생성해서 계속 사용하는 것을 권장
    public static final ObjectMapper MAPPER = new ObjectMapper();


    public static JsonNode jsonToJsonNode(String json) throws JsonProcessingException {
        return MAPPER.readTree(json);
    }
}