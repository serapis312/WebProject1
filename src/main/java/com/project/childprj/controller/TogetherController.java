package com.project.childprj.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.project.childprj.domain.Together;
import com.project.childprj.service.TogetherService;
import com.project.childprj.util.Utils;
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
@RequestMapping("/together")
public class TogetherController {

    private final TogetherService togetherService;
    private JSONParser jsonParser = new JSONParser();

    @Autowired
    public TogetherController(TogetherService togetherService ){
        this.togetherService = togetherService;
    }

    @Value("${app.api.SeoulDataKey}")
    private String SeoulDataKey;

    @GetMapping("/api/{start_index}/{end_index}")
    public ResponseEntity<Integer> getTogetherData(
            @PathVariable("start_index") Integer startIndex,
            @PathVariable("end_index") Integer endIndex
    ) {
        String type = "json"; // 요청 파일 타입
        String service = "culturalEventInfo"; // 서비스명

        String uri = String.format("http://openapi.seoul.go.kr:8088/%s/%s/%s/%d/%d",
                SeoulDataKey, type, service, startIndex, endIndex);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                String jsonData = response.getBody();

                if (jsonData != null && jsonData.length() > 0) {
                    int result = 0;
                    JsonNode rootNode = Utils.jsonToJsonNode(jsonData);
                    ArrayNode rows = (ArrayNode) rootNode.get("culturalEventInfo").get("row");
                    for (JsonNode row : rows) {
                        Together together = Together.fromJson(row);
                        result += togetherService.insertTogether(together);
                    }
                    return ResponseEntity.ok(result);

                } else {
                    // API 응답이 비어있는 경우에 대한 처리
                    return ResponseEntity.status(204).body(null); // No Content
                }
            }else {
                    // 실패했을 경우에 대한 처리
                    return ResponseEntity.status(response.getStatusCode()).body(null);
                }
            } catch(Exception e){
                e.printStackTrace();
                // 예외 처리
                return ResponseEntity.status(500).body(null); // Internal Server Error
            }
        }

        // Thymeleaf 사용해서 list 보여줌
        @GetMapping("/api/{start_index}/{end_index}/list")
        public String showList (Model model,
                @PathVariable("start_index") Integer startIndex,
                @PathVariable("end_index") Integer endIndex) throws JsonProcessingException {
            // 여기에서 유치원 목록을 가져오는 서비스 메서드를 호출하고 모델에 추가하는 로직 추가
            // List<Together> together = TogetherService.getTogether(startIndex, endIndex);
            List<Together> together = togetherService.getAllTogether();
            model.addAttribute("together", together);

            return "togetherList";

        }


//        @PostMapping("/save/api/together")
//        public String insertTogether(
//                @RequestParam("codename") String codename,
//                @RequestParam("guname") String guname,
//                @RequestParam("title") String title,
//                @RequestParam("place") String place,
//                @RequestParam("org_name") String org_name,
//                @RequestParam("use_trgt") String use_trgt,
//                @RequestParam("use_fee") String use_fee,
//                @RequestParam("player") String player,
//                @RequestParam("org_link") String org_link,
//                @RequestParam("main_img") String main_img,
//                @RequestParam("ticket") String ticket,
//                @RequestParam("strtdate") String strtdate,
//                @RequestParam("end_date") String end_date,
//                @RequestParam("themecode") String themecode,
//                @RequestParam("hmpg_addr") String hmpg_addr
//    ){
//
//            Together together = new Together();
//            together.setCODENAME(codename);
//            together.setGUNAME(guname);
//            together.setTITLE(title);
//            together.setPLACE(place);
//            together.setORG_NAME(org_name);
//            together.setUSE_TRGT(use_trgt);
//            together.setUSE_FEE(use_fee);
//            together.setPLAYER(player);
//            together.setORG_LINK(org_link);
//            together.setMAIN_IMG(main_img);
//            together.setTICKET(ticket);
//            together.setSTRTDATE(strtdate);
//            together.setEND_DATE(end_date);
//            together.setTHEMECODE(themecode);
//            together.setHMPG_ADDR(hmpg_addr);
//
//           TogetherService.insertTogether(together);
//
//
//            return "redirect:/index";
//        }
    }

