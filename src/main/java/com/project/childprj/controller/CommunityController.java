package com.project.childprj.controller;

import com.project.childprj.domain.community.Post;
import com.project.childprj.domain.community.PostValidator;
import com.project.childprj.domain.mypage.UserImage;
import com.project.childprj.service.CommunityService;
import com.project.childprj.service.UserService;
import com.project.childprj.util.U;
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

    @GetMapping("/communityWrite")
    public void write(Model model){
        UserImage userImage = communityService.findUserImageByUserId(U.getLoggedUser().getId());
        model.addAttribute("userImage", userImage);
    }

    @PostMapping("/communityWrite")
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
            redirectAttrs.addFlashAttribute("title", post.getTitle());
            redirectAttrs.addFlashAttribute("content", post.getContent());

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList){
                redirectAttrs.addFlashAttribute("error_" + err.getField(), err.getCode());
            }

            return "redirect:/community/communityWrite";
        }

        model.addAttribute("result", communityService.write(post, files));
        return "community/writeOk";

    }

    @GetMapping("/communityDetail/{id}")
    public String detail(@PathVariable(name = "id") Long id, Model model){
        model.addAttribute("post", communityService.detail(id));
        model.addAttribute("recommendCnt", communityService.findRecommendCnt(id));

        Post post = communityService.selectById(id);
        UserImage userImage = communityService.findUserImageByUserId(post.getUser().getId());
        model.addAttribute("userImage", userImage);
        return "community/communityDetail";
    }

    @PostMapping("/recommend")
    public void recommend(){}

    @GetMapping("/communityList")
    public void list(@RequestParam(name = "page", defaultValue = "1") Integer page, Model model){
        communityService.list(page, model);
    }


    @GetMapping("/communityUpdate/{id}")
    public String update(@PathVariable(name = "id") Long id, Model model){
        model.addAttribute("post", communityService.selectById(id));

        Post post = communityService.selectById(id);
        UserImage userImage = communityService.findUserImageByUserId(post.getUser().getId());
        model.addAttribute("userImage", userImage);

        return "community/communityUpdate";
    }

    @PostMapping("/communityUpdate")
    public String updateOk(
            @RequestParam(required = false) Map<String, MultipartFile> files    // 새로 추가될 첨부파일들
            , @RequestParam(required = false) Long[] delfile       // 삭제될 첨부파일들
            , @Valid Post post
            , BindingResult result
            , Model model
            , RedirectAttributes redirectAttrs
    ){
        if(result.hasErrors()){

            // redirect시 기존에 입력했던 값들은 보이게 하기
            redirectAttrs.addFlashAttribute("title", post.getTitle());
            redirectAttrs.addFlashAttribute("content", post.getContent());

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList){
                redirectAttrs.addFlashAttribute("error_" + err.getField(), err.getCode());
            }

            return "redirect:/community/communityUpdate/" + post.getId();
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
