package com.project.childprj.controller;

import com.project.childprj.domain.*;
import com.project.childprj.service.UserService;
import com.project.childprj.util.U;
import jakarta.servlet.http.HttpServletRequest;
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
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public void login(Model model){}

    // onAuthenticationFailure 에서 로그인 실패시 forwarding 용
    // request 에 담겨진 attribute 는 Thymeleaf 에서 그대로 표현 가능.
    @PostMapping("/loginError")
    public String loginError(){
        return "user/login";
    }

    @RequestMapping("/rejectAuth")
    public String rejectAuth(){
        return "common/rejectAuth";
    }

    @GetMapping("/find")
    public void find(){}

//    @GetMapping("/find")
//    public String find(Model model){
//        model.addAttribute("nameAndEmail", new NameAndEmail());
//        model.addAttribute("nameAndLoginId", new NameAndLoginId());
//        return "/user/findOld";
//    }
//
//    @GetMapping("/findOld")
//    public void findOld(){}

    @PostMapping("/findLoginIdByEmail")
    public String findLoginIdByEmailOk(@Valid NameAndEmail nameAndEmail, BindingResult result, Model model, RedirectAttributes redirectAttributes){
        // 검증 에러가 있었다면 redirect 한다.
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("idName", nameAndEmail.getName());
            redirectAttributes.addFlashAttribute("idEmail", nameAndEmail.getEmail());

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList){
                // 가장 처음에 발견된 에러만 보내기
                redirectAttributes.addFlashAttribute("error", err.getCode());
                break;
            }

            return "redirect:/user/find";
        }

        String message = userService.findLoginIdByNameAndEmail(nameAndEmail.getName(), nameAndEmail.getEmail());
        model.addAttribute("message", message);

        return "/user/findLoginIdByEmailOk";
    }

    @PostMapping("/changePasswordByLoginId")
    public String changePasswordByLoginId(@Valid NameAndLoginId nameAndLoginId, BindingResult result, Model model, RedirectAttributes redirectAttributes){
        // 검증 에러가 있었다면 redirect 한다.
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("namebyLoginId", nameAndLoginId.getName());
            redirectAttributes.addFlashAttribute("loginId", nameAndLoginId.getLoginId());

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList){
                // 가장 처음에 발견된 에러만 보내기
                redirectAttributes.addFlashAttribute("error", err.getCode());
                break;
            }

            return "redirect:/user/find";
        }

        if(!userService.isExistByNameAndLoginId(nameAndLoginId)) {
            return "/user/findNot";
        }

        model.addAttribute("name", nameAndLoginId.getName());
        model.addAttribute("loginId", nameAndLoginId.getLoginId());
        return "/user/changePasswordUseLoginId";
    }

    @PostMapping("/changePasswordByEmail")
    public String changePasswordByEmail(@Valid NameAndEmail nameAndEmail, BindingResult result, Model model, RedirectAttributes redirectAttributes){
        // 검증 에러가 있었다면 redirect 한다.
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("nameByEmail", nameAndEmail.getName());
            redirectAttributes.addFlashAttribute("passwordEmail", nameAndEmail.getEmail());

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList){
                // 가장 처음에 발견된 에러만 보내기
                redirectAttributes.addFlashAttribute("error", err.getCode());
                break;
            }

            return "redirect:/user/find";
        }

        if(!userService.isExistByNameAndEmail(nameAndEmail)) {
            return "/user/findNot";
        }

        model.addAttribute("name", nameAndEmail.getName());
        model.addAttribute("email", nameAndEmail.getEmail());
        return "/user/changePasswordUseEmail";
    }

    @GetMapping("/changePasswordUseLoginId")
    public void changePasswordUseLoginId(){}

    @PostMapping("/changePasswordUseLoginId")
    public String changePasswordUseLoginIdOk(@Valid Password password, BindingResult result, NameAndLoginId nameAndLoginId, Model model, RedirectAttributes redirectAttributes){
        // 검증 에러가 있었다면 redirect 한다.
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("name", nameAndLoginId.getName());
            redirectAttributes.addFlashAttribute("loginId", nameAndLoginId.getLoginId());

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList){
                // 가장 처음에 발견된 에러만 보내기
                System.out.println("패스워드 에러는 " + err + " 입니다");
                redirectAttributes.addFlashAttribute("error", err.getCode());
                break;
            }

            return "redirect:/user/changePasswordUseLoginId";
        }

        int cnt = 0;

        String newPassword = password.getNewPassword();
        String re_password = password.getRe_password();

        cnt = userService.changePasswordByLoginId(nameAndLoginId, newPassword, re_password);

        model.addAttribute("result", cnt);
        return "/user/changePasswordOk";
    }

    @GetMapping("/changePasswordUseEmail")
    public void changePasswordUseEmail(){}

    @PostMapping("/changePasswordUseEmail")
    public String changePasswordUseEmailOk(@Valid Password password, BindingResult result, NameAndEmail nameAndEmail, Model model, RedirectAttributes redirectAttributes){
        // 검증 에러가 있었다면 redirect 한다.
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("name", nameAndEmail.getName());
            redirectAttributes.addFlashAttribute("email", nameAndEmail.getEmail());

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList){
                // 가장 처음에 발견된 에러만 보내기
                redirectAttributes.addFlashAttribute("error", err.getCode());
                break;
            }

            return "redirect:/user/changePasswordUseEmail";
        }

        int cnt = 0;

        String newPassword = password.getNewPassword();
        String re_password = password.getRe_password();

        cnt = userService.changePasswordByEmail(nameAndEmail, newPassword, re_password);

        model.addAttribute("result", cnt);
        return "/user/changePasswordOk";
    }

    @GetMapping("/register")
    public void register(){}

    @PostMapping("/register")
    public String registerOk(@Valid User user,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes){

        // 검증 에러가 있었다면 redirect 한다.
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("loginId", user.getLoginId());
            redirectAttributes.addFlashAttribute("name", user.getName());
            redirectAttributes.addFlashAttribute("nickName", user.getNickName());
            redirectAttributes.addFlashAttribute("email", user.getEmail());

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList){
                // 가장 처음에 발견된 에러만 보내기
                redirectAttributes.addFlashAttribute("error", err.getCode());
                break;
            }

            return "redirect:/user/register";
        }

        // 검증 에러 없었으면 회원 등록 진행
        int cnt = userService.register(user);
        model.addAttribute("result", cnt);
        return "/user/registerOk";
    }

    @Autowired
    GenericValidator genericValidator;

    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.setValidator(genericValidator);
    }


}
