package com.project.childprj.controller;

import com.project.childprj.domain.user.MypageValidator;
import com.project.childprj.domain.user.NickName;
import com.project.childprj.domain.user.CheckPassword;
import com.project.childprj.domain.user.MypagePassword;
import com.project.childprj.domain.user.User;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mypage")
public class MypageController {

    @Autowired
    private UserService userService;

    @GetMapping("/main")
    public void main(HttpSession session, Model model){
        String loginId = (String) session.getAttribute("loginId");
        Long userId = 0L;
        if(loginId != null) {
            model.addAttribute("list", userService.findByLoginId(loginId));
            userId = userService.findByLoginId(loginId).getId();
        }
        model.addAttribute("image", userService.findUserImage(userId));
    }

    @PostMapping("/uploadImage")
    public String uploadImageOk(@RequestParam Map<String, MultipartFile> files, @RequestParam(name = "userId") Long userId, Model model){
        MultipartFile file = files.get("upfile");
        int result = userService.uploadProfile(file, userId);
        model.addAttribute("result", result);
        return "mypage/uploadImageOk";
    }

    @PostMapping("/changePassword")
    public String changePasswordOk(@Valid MypagePassword mypagePassword, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList) {
                redirectAttributes.addFlashAttribute("error_password", err.getCode());
                break;
            }

            redirectAttributes.addFlashAttribute("flag", 0);
            return "redirect:/mypage/main";
        }

        model.addAttribute("result", userService.changePassword(mypagePassword));
        return "mypage/changePasswordOk";
    }

    @PostMapping("/updateNickName")
    public String updateOk(@Valid NickName nickName, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        // 검증 에러가 있었다면 redirect 한다.
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("userId", nickName.getUserId());
            redirectAttributes.addFlashAttribute("nickName", nickName.getNickName());
            redirectAttributes.addFlashAttribute("flagNickName", 0);

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList){
                // 가장 처음에 발견된 에러만 보내기
                redirectAttributes.addFlashAttribute("error_nickName", err.getCode());
                break;
            }

            return "redirect:/mypage/main";
        }

        // 검증 에러 없었으면 닉네임 변경 진행
        int cnt = userService.updateNickName(nickName);
        model.addAttribute("result", cnt);
        return "mypage/updateNickNameOk";
    }

    @GetMapping("/delete")
    public void delete(@RequestParam(name = "userId") Long userId, Model model){
        model.addAttribute("userId", userId);
    }

    @PostMapping("/delete")
    public String deleteOk(@Valid CheckPassword checkPassword, BindingResult result, Model model, RedirectAttributes redirectAttributes){
        // 검증 에러가 있었다면 redirect 한다.
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("flagDelete", 0);

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList){
                // 가장 처음에 발견된 에러만 보내기
                redirectAttributes.addFlashAttribute("error_delete", err.getCode());
                break;
            }

            return "redirect:/mypage/main";
        }

        User user = userService.findById(checkPassword.getUserId());
        user.setPassword(checkPassword.getOriginPassword());
        user.setRe_password(checkPassword.getRe_password());
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
