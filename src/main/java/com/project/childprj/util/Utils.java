package com.project.childprj.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {
	public static final ObjectMapper MAPPER = new ObjectMapper();
	
	public static JsonNode jsonToJsonNode(String json) throws JsonProcessingException {
		return MAPPER.readTree(json);
	}
}

