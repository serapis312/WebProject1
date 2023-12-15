package com.project.childprj.service;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
KinderC 에대한 service 로직
임의의 파일임으로 수정 및 삭제 예정
 */
@Service
public class KinderCService {

    @Value("${app.api.kinderKey}")
    private String kinderKey;

    public List<Map<String, Object>> getKinder(Integer startIndex, Integer endIndex) {
        String type = "json";
        String service = "childSchoolInfo";

        String uri = String.format("http://openapi.seoul.go.kr:8088/%s/%s/%s/%d/%d",
                kinderKey,type, service, startIndex, endIndex);
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
