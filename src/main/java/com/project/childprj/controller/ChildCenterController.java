package com.project.childprj.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.childprj.service.ChildCenterService;

import lombok.RequiredArgsConstructor;

/*
아동센터 목록 출력 완성
200개의 api를 가져옴
 */

@Controller
@RequestMapping("/protect")
@RequiredArgsConstructor	// 필수 멤버 생성자
public class ChildCenterController {

	private final ChildCenterService childCenterService;
	
	@GetMapping("/get/child-center/list")
    public String showList(Model model){

		int startIndex = 1;
		int endIndex = 200;
		
        List<Map<String, Object>> childCenter = childCenterService.getChildCenter(startIndex, endIndex);
        model.addAttribute("childCenter", childCenter);

        return "childCenterList";
    }
	
	@GetMapping("/get/child-center")
	public ResponseEntity<List<Map<String, Object>>> test() {
		int startIndex = 1;
		int endIndex = 200;
		return ResponseEntity.ok(childCenterService.getChildCenter(startIndex, endIndex));
	}

}