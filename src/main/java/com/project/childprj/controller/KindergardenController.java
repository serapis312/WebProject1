package com.project.childprj.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.childprj.domain.Kindergarden;
import com.project.childprj.service.KindergardenService;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*

2023-12-15 기준
: error 발생 -> 현재 api를 못가져옴 + 데이터베이스 저장도 안됨.

그래서 임의로 controller 에 KinderC 라는 파일추가해서 api 가져오는것만 시도완료

*/
@Controller
@RequestMapping("/protect")
public class KindergardenController {

    private final KindergardenService kindergardenService;
    private JSONParser jsonParser = new JSONParser();


    @Autowired
    public KindergardenController(KindergardenService kindergardenService) {
        this.kindergardenService = kindergardenService;
    }


// Thymeleaf 사용해서 list 보여줌
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

    @PostMapping("/save/api/kindergarden")
    public String insertKindergarden(
            @RequestParam("name") String name,
            @RequestParam("establish") String establish,
            @RequestParam("ldgrName") String ldgrName,
            @RequestParam("addr") String addr,
            @RequestParam("telNo") String telNo,
            @RequestParam("hpAddr") String hpAddr,
            @RequestParam("operTime") String operTime
    ) {
        Kindergarden kindergarden = new Kindergarden();
        kindergarden.setKINDERNAME(name);
        kindergarden.setESTABLISH(establish);
        kindergarden.setLDGRNAME(ldgrName);
        kindergarden.setADDR(addr);
        kindergarden.setTELNO(telNo);
        kindergarden.setHPADDR(hpAddr);
        kindergarden.setOPERTIME(operTime);

        kindergardenService.insertKindergarden(kindergarden);
        return "redirect:/index";
    }
}

