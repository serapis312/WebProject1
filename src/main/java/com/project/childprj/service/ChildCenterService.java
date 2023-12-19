package com.project.childprj.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@NoArgsConstructor
public class ChildCenterService {

	@Value("${app.api.childCenterKey}")
	private String childCenterKey;

	public List<Map<String, Object>> getChildCenter(Integer startIndex, Integer endIndex) {
		String type = "json";
		String service = "TnFcltySttusInfo1003";

		String uri = String.format("http://openapi.seoul.go.kr:8088/%s/%s/%s/%d/%d",
				childCenterKey, type, service, startIndex, endIndex);

		RestTemplate restTemplate = new RestTemplate();
		// BODY를 JSON 문자열 대신 JSONObject
		ResponseEntity<JSONObject> response = restTemplate.getForEntity(uri, JSONObject.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			JSONObject body = response.getBody();
			if (body != null && !body.isEmpty()) {
				Map<String, Object> data = (Map<String, Object>) body.get(service);

				// json에서 row키를 추출해서 List 형태로 반환
				return (List<Map<String, Object>>) data.get("row");
        	}
		}

		return new ArrayList<>();
	}

}
