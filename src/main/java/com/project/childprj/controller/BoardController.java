package com.project.childprj.controller;

import com.project.childprj.domain.Post;
import com.project.childprj.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/list")
    public void list(Model model){
        model.addAttribute("list", boardService.list());
    }

    @GetMapping("/write")
    public void write(){}

    @PostMapping("/write")
    public String writeOk(
            @Validated Post post, BindingResult result , Model model , RedirectAttributes redirectAttributes
    ){
        if (result.hasErrors()) {
            // redirect 시 기존에 입력했던 값들은 보이게 하기
            redirectAttributes.addFlashAttribute("name", post.getName());
            redirectAttributes.addFlashAttribute("subject", post.getTitle());
            redirectAttributes.addFlashAttribute("content", post.getContent());

            // validation 에러를 flash attribute로 전달
            List<FieldError> errList = result.getFieldErrors();
            for (FieldError err : errList) {
                redirectAttributes.addFlashAttribute("error_" + err.getField(), err.getCode());
            }

            return "redirect:/board/write";
        }

        // validation 에러가 없으면 여기에 원하는 로직을 추가하여 글을 저장하거나 처리할 수 있습니다.

        return "redirect:/board/writeOk";  // 성공 페이지로 리다이렉트
    }

    // todo:
    //  1. getMapping("/update")
    //  2. 유치원 api 가져오기
    //  3. error 수정

}
