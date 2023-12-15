package com.project.childprj.controller;

import com.project.childprj.service.KinderCService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/*
Kindergarden api를 임의로 api를 가져와서 프론트단에서 출력해주는 코드
*/

@Controller
@RequestMapping("/protect")
@RequiredArgsConstructor    // 필수 멤버 생성자
public class KinderC {
    private final KinderCService kinderCService;

    @GetMapping("/get/kinder/list")
    public String showKinder(Model model){
        int startIndex = 1;
        int endIndex = 200;

        List<Map<String , Object>> kinder = kinderCService.getKinder(startIndex,endIndex);
        model.addAttribute("kinder",kinder);

        return "kinderList";
    }

    @GetMapping("/get/kinder")
    public ResponseEntity<List<Map<String,Object>>> test(){
        int startIndex = 1;
        int endIndex = 200;
        return ResponseEntity.ok(kinderCService.getKinder(startIndex,endIndex));
    }
}
