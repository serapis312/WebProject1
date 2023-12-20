package com.project.childprj.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.childprj.DTO.ChildHouseDTO;
import com.project.childprj.domain.ChildHouse;
import com.project.childprj.service.ChildHouseService;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequestMapping("/protect")
public class ChildHouseController {

    private ChildHouseService childHouseService;
    private JSONParser jsonParser = new JSONParser();

    @Autowired
    public ChildHouseController(ChildHouseService childHouseService) {
        this.childHouseService = childHouseService;
    }

    @Value("${app.api.kinderKey}")
    private String childHouseKey;

    @GetMapping("/api/childhouse/{start_index}/{end_index}")
    public ResponseEntity<List<ChildHouseDTO>> getChildHouseData(
                @PathVariable("start_index") Integer startIndex,
                @PathVariable("end_index") Integer endIndex
        ) {

            String type = "json"; // 요청 파일 타입
            String service = "ChildCareInfo"; // 서비스명

        String uri = String.format("http://openapi.seoul.go.kr:8088/%s/%s/%s/%d/%d",
                childHouseKey, type, service, startIndex, endIndex);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                String jsonData = response.getBody();

                if (jsonData != null && jsonData.length() > 0) {
                    List<ChildHouseDTO> childHouseDTO = ChildHouseDTO.fromJson(jsonData);
                    return ResponseEntity.ok(childHouseDTO);
                } else {
                    // API 응답이 비어있는 경우에 대한 처리
                    return ResponseEntity.status(204).body(null); // No Content
                }
            } else {
                // 실패했을 경우에 대한 처리
                return ResponseEntity.status(response.getStatusCode()).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 예외 처리
            return ResponseEntity.status(500).body(null); // Internal Server Error
        }
    }

    @GetMapping("/api/childhouse/list")
    public String showList(Model model) throws JsonProcessingException {
        // 여기에서 유치원 목록을 가져오는 서비스 메서드를 호출하고 모델에 추가하는 로직 추가

        // 데이터베이스에 데이터를 추가
//        List<ChildHouse> childHouse = childHouseService.getChildHouse(startIndex, endIndex);

        // 저장된 데이터베이스를 가져와서 목록 보여줌
        List<ChildHouse> childHouse = childHouseService.getAllChildHouse();
        model.addAttribute("childHouse", childHouse);

        return "protect/childHouseList";
    }

    @GetMapping("/api/childhouse/{crname}/detail")
    public String showDetail(Model model,
//                            @PathVariable("start_index") Integer startIndex,
//                            @PathVariable("end_index") Integer endIndex,
                             @PathVariable("crname") String crname) throws JsonProcessingException {
//         여기에서 유치원 목록을 가져오는 서비스 메서드를 호출하고 모델에 추가하는 로직 추가
//        List<ChildHouse> childHouse = childHouseService.getChildHouse(startIndex, endIndex);
        List<ChildHouse> childHouse = childHouseService.getAllChildHouse();
        model.addAttribute("childHouse", childHouse);

        return "childHouseDetail";
    }

}
