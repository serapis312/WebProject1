package com.project.childprj.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
public class KinderGarden {

    @Value("${app.api.SeoulDataKey}")
    private String SeoulDataKey;

    @GetMapping("/api/kindergarden/{start_index}/{end_index}")
    public ResponseEntity<String> kindergarden(
            @PathVariable("start_index") Integer startIndex,
            @PathVariable("end_index") Integer endIndex
    ) {
        String type = "json"; // 요청 파일 타입
        String service = "childSchoolInfo"; // 서비스명

        String uri = String.format("http://openapi.seoul.go.kr:8088/%s/%s/%s/%d/%d",
                SeoulDataKey, type, service, startIndex, endIndex);

        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.getForEntity(uri, String.class);

        return response;
    }
}


