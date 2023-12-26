package com.project.childprj.controller;

import com.project.childprj.domain.user.*;
import com.project.childprj.service.UserService;
import com.project.childprj.util.U;
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
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/mypage")
    public void mypage(HttpSession session, Model model){
        String username = (String) session.getAttribute("username");
        Long user_id = 0L;
        if(username != null) {
            model.addAttribute("list", userService.findByUsername(username));
            user_id = userService.findByUsername(username).getId();
        }
        model.addAttribute("image", userService.findUserImage(user_id));
    }

    @PostMapping("/uploadImage")
    public String uploadImageOk(@RequestParam Map<String, MultipartFile> files, @RequestParam(name = "userId") Long userId, Model model){
        MultipartFile file = files.get("upfile");
        int result = userService.uploadProfile(file, userId);
        model.addAttribute("result", result);
        return "user/uploadImageOk";
    }

    @PostMapping("/changePassword")
    public String changePasswordOk(@Valid MypagePassword mypagePassword, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            redirectAttributes.addFlashAttribute("flag", 0);

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList) {
                redirectAttributes.addFlashAttribute("error_password", err.getCode());
                break;
            }

            redirectAttributes.addFlashAttribute("flag", 0);
            return "redirect:/user/mypage";
        }

        model.addAttribute("result", userService.changePassword(mypagePassword));
        return "user/changePasswordOk";
    }

    @PostMapping("/updateNickname")
    public String updateOk(@Valid Nickname nickname, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        // 검증 에러가 있었다면 redirect 한다.
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("user_id", nickname.getUser_id());
            redirectAttributes.addFlashAttribute("nickname", nickname.getNickname());
            redirectAttributes.addFlashAttribute("flagNickname", 0);

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList){
                // 가장 처음에 발견된 에러만 보내기
                redirectAttributes.addFlashAttribute("error_nickname", err.getCode());
                break;
            }

            return "redirect:/user/mypage";
        }

        // 검증 에러 없었으면 닉네임 변경 진행
        int cnt = userService.updateNickName(nickname);
        model.addAttribute("result", cnt);
        return "user/updateNicknameOk";
    }

    @GetMapping("/delete")
    public void delete(@RequestParam(name = "user_id") Long user_id, Model model){
        model.addAttribute("user_id", user_id);
    }

    @PostMapping("/delete")
    public String deleteOk(@Valid CheckPassword checkPassword, BindingResult result, Model model, RedirectAttributes redirectAttributes){
        // 검증 에러가 있었다면 redirect 한다.
        if(result.hasErrors()){

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList){
                // 가장 처음에 발견된 에러만 보내기
                redirectAttributes.addFlashAttribute("error_delete", err.getCode());
                break;
            }

            return "redirect:/user/mypage";
        }

        User user = userService.findById(checkPassword.getUser_id());
        user.setPassword(checkPassword.getOriginPassword());
        user.setRe_password(checkPassword.getRe_password());
        model.addAttribute("result", userService.deleteUser(user));
        return "user/deleteOk";
    }

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

    @PostMapping("/findUsernameByEmail")
    public String findUsernameByEmailOk(@Valid NameAndEmail nameAndEmail, BindingResult result, @RequestParam(name = "index") String index,  Model model, RedirectAttributes redirectAttributes){
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

        String message = userService.findUsernameByNameAndEmail(nameAndEmail);
        model.addAttribute("message", message);

        return "/user/findUsernameByEmailOk";
    }

    @GetMapping("/findNot")
    public void findNot(@RequestParam(name = "index") String index, Model model){
        model.addAttribute("index", index);
    }

    @PostMapping("/changePasswordByUsername")
    public String changePasswordByUsername(@Valid NameAndUsername nameAndUsername, BindingResult result, @RequestParam(name = "index") String index, Model model, RedirectAttributes redirectAttributes){
        // 검증 에러가 있었다면 redirect 한다.
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("namebyUsername", nameAndUsername.getName());
            redirectAttributes.addFlashAttribute("username", nameAndUsername.getUsername());
            model.addAttribute("index", index);

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList){
                // 가장 처음에 발견된 에러만 보내기
                redirectAttributes.addFlashAttribute("error_findById", err.getCode());
                break;
            }

            return "redirect:/user/find?index=" + index;
        }

        if(!userService.isExistByNameAndUsername(nameAndUsername)) {
            model.addAttribute("index", index);
            return "/user/findNot";
        }
        String username = nameAndUsername.getUsername();

        model.addAttribute("name", nameAndUsername.getName());
        model.addAttribute("username", nameAndUsername.getUsername());
        model.addAttribute("user_id", userService.findByUsername(username).getId());
        return "/user/changePasswordUseUsername";
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
        model.addAttribute("user_id", userService.findByEmail(email));
        return "/user/changePasswordUseEmail";
    }

    @GetMapping("/changePasswordUseUsername")
    public String changePasswordUseUsername(){
        if(U.getRequest().getHeader("REFERER") == null) {
            return "redirect:/user/find";
        } else if(U.getRequest().getHeader("REFERER").equals("http://localhost:8090/user/changePasswordByUsername")) {
            return "/user/changePasswordUseUsername";
        }

        return "/user/changePasswordUseUsername";
    }

    @PostMapping("/changePasswordUseUsername")
    public String changePasswordUseUsernameOk(@Valid NewPassword newPasswordInstance, @Valid NameAndUsername nameAndUsername, BindingResult result, Model model, RedirectAttributes redirectAttributes){
        // 검증 에러가 있었다면 redirect 한다.
        if(result.hasErrors()){

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList){
                // 가장 처음에 발견된 에러만 보내기
                System.out.println("패스워드 에러는 " + err + " 입니다");
                redirectAttributes.addFlashAttribute("error_changePasswordByUsername", err.getCode());
                break;
            }

            return "redirect:/user/changePasswordUseUsername";
        }

        int cnt = 0;

        cnt = userService.changePasswordByUsername(nameAndUsername, newPasswordInstance);

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
        } else if(U.getRequest().getHeader("REFERER").equals("/user/signUpAgree")) {
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
            redirectAttributes.addFlashAttribute("username", user.getUsername());
            redirectAttributes.addFlashAttribute("name", user.getName());
            redirectAttributes.addFlashAttribute("nickname", user.getNickname());
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
