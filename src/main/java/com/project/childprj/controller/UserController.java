package com.project.childprj.controller;

import com.project.childprj.domain.user.*;
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

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/signIn")
    public void login(){}

    // onAuthenticationFailure 에서 로그인 실패시 forwarding 용
    // request 에 담겨진 attribute 는 Thymeleaf 에서 그대로 표현 가능.
    @PostMapping("/loginError")
    public String loginError(){
        return "user/signIn";
    }

    @RequestMapping("/rejectAuth")
    public String rejectAuth(){
        return "common/rejectAuth";
    }

    @GetMapping("/find")
    public void find(@RequestParam(name = "index", defaultValue = "") String index, Model model){
        model.addAttribute("index", index);
    }

    @PostMapping("/findLoginIdByEmail")
    public String findLoginIdByEmailOk(@Valid NameAndEmail nameAndEmail, BindingResult result, @RequestParam(name = "index") String index,  Model model, RedirectAttributes redirectAttributes){
        // 검증 에러가 있었다면 redirect 한다.
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("idName", nameAndEmail.getName());
            redirectAttributes.addFlashAttribute("idEmail", nameAndEmail.getEmail());
            model.addAttribute("index", index);

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList){
                // 가장 처음에 발견된 에러만 보내기
                redirectAttributes.addFlashAttribute("error_findId", err.getCode());
                break;
            }

            return "redirect:/user/find?index=" + index;
        }

        String message = userService.findLoginIdByNameAndEmail(nameAndEmail);
        model.addAttribute("message", message);

        return "/user/findLoginIdByEmailOk";
    }

    @GetMapping("/findNot")
    public void findNot(@RequestParam(name = "index") String index, Model model){
        model.addAttribute("index", index);
    }

    @PostMapping("/changePasswordByLoginId")
    public String changePasswordByLoginId(@Valid NameAndLoginId nameAndLoginId, BindingResult result, @RequestParam(name = "index") String index, Model model, RedirectAttributes redirectAttributes){
        // 검증 에러가 있었다면 redirect 한다.
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("namebyLoginId", nameAndLoginId.getName());
            redirectAttributes.addFlashAttribute("loginId", nameAndLoginId.getLoginId());
            model.addAttribute("index", index);

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList){
                // 가장 처음에 발견된 에러만 보내기
                redirectAttributes.addFlashAttribute("error_findById", err.getCode());
                break;
            }

            return "redirect:/user/find?index=" + index;
        }

        if(!userService.isExistByNameAndLoginId(nameAndLoginId)) {
            model.addAttribute("index", index);
            return "/user/findNot";
        }
        String loginId = nameAndLoginId.getLoginId();

        model.addAttribute("name", nameAndLoginId.getName());
        model.addAttribute("loginId", loginId);
        model.addAttribute("userId", userService.findByLoginId(loginId).getId());
        return "/user/changePasswordUseLoginId";
    }

    @PostMapping("/changePasswordByEmail")
    public String changePasswordByEmail(@Valid NameAndEmail nameAndEmail, BindingResult result, @RequestParam(name = "index") String index, Model model, RedirectAttributes redirectAttributes){
        // 검증 에러가 있었다면 redirect 한다.
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("nameByEmail", nameAndEmail.getName());
            redirectAttributes.addFlashAttribute("passwordEmail", nameAndEmail.getEmail());
            model.addAttribute("index", index);

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList){
                // 가장 처음에 발견된 에러만 보내기
                redirectAttributes.addFlashAttribute("error_findByEmail", err.getCode());
                break;
            }

            return "redirect:/user/find?index=" + index;
        }

        if(!userService.isExistByNameAndEmail(nameAndEmail)) {
            model.addAttribute("index", index);
            return "/user/findNot";
        }
        String email = nameAndEmail.getEmail();

        model.addAttribute("name", nameAndEmail.getName());
        model.addAttribute("email", email);
        model.addAttribute("userId", userService.findByEmail(email));
        return "/user/changePasswordUseEmail";
    }

    @GetMapping("/changePasswordUseLoginId")
    public String changePasswordUseLoginId(){
        if(U.getRequest().getHeader("REFERER") == null) {
            return "redirect:/user/find";
        } else if(U.getRequest().getHeader("REFERER").equals("http://localhost:8090/user/changePasswordByLoginId")) {
            return "/user/changePasswordUseLoginId";
        }

        return "/user/changePasswordUseLoginId";
    }

    @PostMapping("/changePasswordUseLoginId")
    public String changePasswordUseLoginIdOk(@Valid NewPassword newPasswordInstance, @Valid NameAndLoginId nameAndLoginId, BindingResult result, Model model, RedirectAttributes redirectAttributes){
        // 검증 에러가 있었다면 redirect 한다.
        if(result.hasErrors()){

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList){
                // 가장 처음에 발견된 에러만 보내기
                System.out.println("패스워드 에러는 " + err + " 입니다");
                redirectAttributes.addFlashAttribute("error_changePasswordByLoginId", err.getCode());
                break;
            }

            return "redirect:/user/changePasswordUseLoginId";
        }

        int cnt = 0;

        cnt = userService.changePasswordByLoginId(nameAndLoginId, newPasswordInstance);

        model.addAttribute("result", cnt);
        return "/user/changePasswordOk";
    }

    @GetMapping("/changePasswordUseEmail")
    public String changePasswordUseEmail(){
        if(U.getRequest().getHeader("REFERER") == null) {
            return "redirect:/user/find";
        } else if(U.getRequest().getHeader("REFERER").equals("http://localhost:8090/user/changePasswordByEmail")) {
            return "/user/changePasswordUseEmail";
        }

        return "/user/changePasswordUseEmail";
    }

    @PostMapping("/changePasswordUseEmail")
    public String changePasswordUseEmailOk(@Valid NewPassword newPasswordInstance, @Valid NameAndEmail nameAndEmail, BindingResult result, Model model, RedirectAttributes redirectAttributes){
        // 검증 에러가 있었다면 redirect 한다.
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("name", nameAndEmail.getName());
            redirectAttributes.addFlashAttribute("email", nameAndEmail.getEmail());

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList){
                // 가장 처음에 발견된 에러만 보내기
                redirectAttributes.addFlashAttribute("error_changePasswordByEmail", err.getCode());
                break;
            }

            return "redirect:/user/changePasswordUseEmail";
        }

        int cnt = 0;

        cnt = userService.changePasswordByEmail(nameAndEmail, newPasswordInstance);

        model.addAttribute("result", cnt);
        return "/user/changePasswordOk";
    }

    @GetMapping("/signUpAgree")
    public void signUpAgree(){}

    @GetMapping("/signUp")
    public String signUp(){
        if(U.getRequest().getHeader("REFERER") == null) {
            return "redirect:/user/signUpAgree";
        } else if(U.getRequest().getHeader("REFERER").equals("http://localhost:8090/user/signUpAgree")) {
            return "/user/signUp";
        }

        return "/user/signUp";
    }

    @PostMapping("/signUp")
    public String signUpOk(@Valid User user,
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

            return "redirect:/user/signUp";
        }

        // 검증 에러 없었으면 회원 등록 진행
        int cnt = userService.register(user);
        model.addAttribute("result", cnt);
        return "/user/signUpOk";
    }

    @Autowired
    GenericValidator genericValidator;

    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.setValidator(genericValidator);
    }


}
