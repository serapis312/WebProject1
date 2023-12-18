package com.project.childprj.controller;

import com.project.childprj.domain.community.Post;
import com.project.childprj.domain.community.PostValidator;
import com.project.childprj.service.CommunityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/community")
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    @GetMapping("/write")
    public void write(){}

    @PostMapping("/write")
    public String writeOk(
            @RequestParam Map<String, MultipartFile> files,
            @Valid Post post
            , BindingResult result
            , Model model
            , RedirectAttributes redirectAttrs
    ){
        // validation 에러가 있었다면 redirect 한다!
        if(result.hasErrors()){

            // redirect시 기존에 입력했던 값들은 보이게 하기
            redirectAttrs.addFlashAttribute("user", post.getUser());
            redirectAttrs.addFlashAttribute("subject", post.getTitle());
            redirectAttrs.addFlashAttribute("content", post.getContent());

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList){
                redirectAttrs.addFlashAttribute("error_" + err.getField(), err.getCode());
            }

            return "redirect:/community/write";
        }

        model.addAttribute("result", communityService.write(post, files));
        return "community/writeOk";

    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model){
        model.addAttribute("post", communityService.detail(id));
        return "community/detail";
    }


    @GetMapping("/list")
    public void list(Integer page, Model model){
        communityService.list(page, model);
    }


    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model){
        model.addAttribute("post", communityService.selectById(id));
        return "community/update";
    }

    @PostMapping("/update")
    public String updateOk(
            @RequestParam Map<String, MultipartFile> files    // 새로 추가될 첨부파일들
            , Long[] delfile       // 삭제될 첨부파일들
            , @Valid Post post
            , BindingResult result
            , Model model
            , RedirectAttributes redirectAttrs
    ){
        if(result.hasErrors()){

            // redirect시 기존에 입력했던 값들은 보이게 하기
            redirectAttrs.addFlashAttribute("subject", post.getTitle());
            redirectAttrs.addFlashAttribute("content", post.getContent());

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList){
                redirectAttrs.addFlashAttribute("error_" + err.getField(), err.getCode());
            }

            return "redirect:/community/update/" + post.getId();
        }


        model.addAttribute("result", communityService.update(post, files, delfile));
        return "community/updateOk";
    }

    @PostMapping("/delete")
    public String deleteOk(Long id, Model model){
        model.addAttribute("result", communityService.deleteById(id));
        return "community/deleteOk";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.setValidator(new PostValidator());
    }
}
