package com.project.childprj.controller;

import com.project.childprj.domain.post.Post;
import com.project.childprj.domain.post.PostValidator;
import com.project.childprj.domain.post.Recommend;
import com.project.childprj.domain.user.UserImage;
import com.project.childprj.service.PostService;
import com.project.childprj.util.U;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/write")
    public void write(Model model){
        UserImage userImage = postService.findUserImageByUserId(U.getLoggedUser().getId());
        model.addAttribute("userImage", userImage);
    }

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
            redirectAttrs.addFlashAttribute("title", post.getTitle());
            redirectAttrs.addFlashAttribute("content", post.getContent());

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList){
                redirectAttrs.addFlashAttribute("error_" + err.getField(), err.getCode());
            }

            return "redirect:/post/write";
        }

        model.addAttribute("result", postService.write(post, files));
        return "post/writeOk";

    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable(name = "id") Long id, Model model){
        model.addAttribute("post", postService.detail(id));

        UserImage userImageComment = postService.findUserImageByUserId(U.getLoggedUser().getId());
        model.addAttribute("userImageComment", userImageComment);

        Post post = postService.selectById(id);
        UserImage userImageDetail = postService.findUserImageByUserId(post.getUser().getId());
        model.addAttribute("userImageDetail", userImageDetail);

        return "post/detail";
    }

    @PostMapping("/recommend")
    public String recommend(@RequestParam("userId") Long userId,@RequestParam("postId") Long postId, Model model){
        int result = postService.addRecommend(userId, postId);

        model.addAttribute("result", result);
        model.addAttribute("postId", postId);

        return "post/recommendOk";
    }

    @GetMapping("/list")
    public void list(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "orderWay", required = false) String orderWay,
            @RequestParam(name = "searchTxt", required = false) String searchTxt,
            Model model
    ){
        if((orderWay == null || orderWay.isEmpty()) && (searchTxt == null || searchTxt.isEmpty())) {
            postService.list(page, model);
        } else {
            postService.listPost(page, orderWay, searchTxt, model);
        }
    }


    @GetMapping("/update/{id}")
    public String update(@PathVariable(name = "id") Long id, Model model){
        model.addAttribute("post", postService.selectById(id));

        Post post = postService.selectById(id);
        UserImage userImage = postService.findUserImageByUserId(post.getUser().getId());
        model.addAttribute("userImage", userImage);

        return "post/update";
    }

    @PostMapping("/update")
    public String updateOk(
            @RequestParam Map<String, MultipartFile> files    // 새로 추가될 첨부파일들
            , @RequestParam Long[] delfile       // 삭제될 첨부파일들
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

            return "redirect:/post/update/" + post.getId();
        }

        model.addAttribute("result", postService.update(post, files, delfile));
        return "post/updateOk";
    }

    @PostMapping("/delete")
    public String deleteOk(Long id, Model model){
        model.addAttribute("result", postService.deleteById(id));
        return "post/deleteOk";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.setValidator(new PostValidator());
    }
}
