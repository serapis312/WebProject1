package com.project.childprj.controller;

import com.project.childprj.domain.GenericValidator;
import com.project.childprj.domain.MypageValidator;
import com.project.childprj.domain.User;
import com.project.childprj.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/mypage")
public class MypageController {

    @Autowired
    private UserService userService;

    @GetMapping("/main/{id}")
    public String mypage(@PathVariable Long id, Model model){
        model.addAttribute("list", userService.findById(id));
        return "mypage/main";
    }

    @GetMapping("/updateNickName")
    public void update(User user, Model model){
        model.addAttribute("password", user.getPassword());
    }

    @PostMapping("/updateNickName")
    public String updateOk(@Valid User user, BindingResult result, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        // 검증 에러가 있었다면 redirect 한다.
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("nickName", user.getNickName());
            redirectAttributes.addAttribute("password", user.getPassword());

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList){
                // 가장 처음에 발견된 에러만 보내기
                redirectAttributes.addFlashAttribute("error", err.getCode());
                break;
            }

            return "redirect:/mypage/update";
        }

        // 검증 에러 없었으면 회원 수정 진행
        int cnt = userService.updateUser(user, session);
        model.addAttribute("result", cnt);
        return "/mypage/updateOk";
    }

    @GetMapping("/delete")
    public void delete(User user, Model model){
        model.addAttribute("nickName", user.getNickName());
        model.addAttribute("id", user.getId());
    }

    @PostMapping("/delete")
    public String deleteOk(@Valid User user, BindingResult result, Model model, RedirectAttributes redirectAttributes){
        // 검증 에러가 있었다면 redirect 한다.
        if(result.hasErrors()){
            redirectAttributes.addAttribute("nickName", user.getNickName());
            redirectAttributes.addAttribute("id", user.getId());

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList){
                // 가장 처음에 발견된 에러만 보내기
                redirectAttributes.addFlashAttribute("error", err.getCode());
                break;
            }

            return "redirect:/mypage/delete";
        }

        model.addAttribute("result", userService.deleteUser(user));
        return "mypage/deleteOk";
    }


    @Autowired
    MypageValidator mypageValidator;

    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.setValidator(mypageValidator);
    }
}
