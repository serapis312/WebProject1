package com.project.childprj.controller;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.project.childprj.util.Utils;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.childprj.domain.Kindergarden;
import com.project.childprj.service.KindergardenService;

@Controller
@RequestMapping("/protect")
public class KindergardenController {

    private final KindergardenService kindergardenService;
    private JSONParser jsonParser = new JSONParser();


    @Autowired
    public KindergardenController(KindergardenService kindergardenService) {
        this.kindergardenService = kindergardenService;
    }

    @Value("${app.api.kinderKey}")
    private String kinderKey;

    @GetMapping("/api/kindergarden/{start_index}/{end_index}")
    public ResponseEntity<Integer> getKindergardenData(
            @PathVariable("start_index") Integer startIndex,
            @PathVariable("end_index") Integer endIndex
    ) {
        String type = "json"; // 요청 파일 타입
        String service = "childSchoolInfo"; // 서비스명

        String uri = String.format("http://openapi.seoul.go.kr:8088/%s/%s/%s/%d/%d",
                kinderKey, type, service, startIndex, endIndex);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                String jsonData = response.getBody();
                if (jsonData != null && jsonData.length() > 0) {
                    int result = 0;
                    JsonNode rootNode = Utils.jsonToJsonNode(jsonData);
                    // childSchoolInfo > row 데이터를 꺼냄
                    ArrayNode rows = (ArrayNode) rootNode.get("childSchoolInfo").get("row");
                    for (JsonNode row : rows) {
                        Kindergarden kindergarden = Kindergarden.fromJson(row);
                        result += kindergardenService.insertKindergarden(kindergarden);
                    }
                    return ResponseEntity.ok(result);
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


    @GetMapping("/api/kindergarden/{start_index}/{end_index}/list")
    public String showList(Model model,
                           @PathVariable("start_index") Integer startIndex,
                           @PathVariable("end_index") Integer endIndex) throws JsonProcessingException {
        // 여기에서 유치원 목록을 가져오는 서비스 메서드를 호출하고 모델에 추가하는 로직 추가
//    	List<Kindergarden> kindergarden = kindergardenService.getKindergarden(startIndex, endIndex);
        List<Kindergarden> kindergarden = kindergardenService.getAllKindergarden();
        model.addAttribute("kindergarden", kindergarden);

        return "kindergardenList";
    }

    // 상세 조회
    @GetMapping("/api/kindergarden")
    public String showDetail(Model model, @RequestParam("kindername") String kindername) {
        Kindergarden kindergarden = kindergardenService.getKindergarden(kindername);
        model.addAttribute("kindergarden", kindergarden);

        return "kindergardenDetail";
    }


    @PostMapping("/save/api/kindergarden")
    public String insertKindergarden(
            @RequestParam("kindername") String kindername,
            @RequestParam("establish") String establish,
            @RequestParam("ldgrName") String ldgrName,
            @RequestParam("addr") String addr,
            @RequestParam("telNo") String telNo,
            @RequestParam("hpAddr") String hpAddr,
            @RequestParam("operTime") String operTime
    ) {
        Kindergarden kindergarden = new Kindergarden();
        kindergarden.setKINDERNAME(kindername);
        kindergarden.setESTABLISH(establish);
        kindergarden.setLDGRNAME(ldgrName);
        kindergarden.setADDR(addr);
        kindergarden.setTELNO(telNo);
        kindergarden.setHPADDR(hpAddr);
        kindergarden.setOPERTIME(operTime);

        kindergardenService.insertKindergarden(kindergarden);
        return "kindergardenDetail";
    }
}

