package com.project.childprj.domain.user;

import com.project.childprj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class UserValidator implements Validator {

    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        String username = user.getUsername();
        String name = user.getName();
        String email = user.getEmail();
        String nickname = user.getNickname();
        String password = user.getPassword();

        // 이미 DB 에 존재하는지 확인
        if (userService.isExist(username)) {
            errors.rejectValue("username", "이미 존재하는 아이디 입니다");
        }
        if (userService.isExistByEmail(email)) {
            errors.rejectValue("email", "이미 존재하는 이메일 입니다");
        }
        if (userService.isExistByNickName(nickname)) {
            errors.rejectValue("nickname", "이미 존재하는 닉네임 입니다");
        }

        // username
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "아이디는 필수입니다");
        if (username.length() < 8) {
            errors.rejectValue("username", "아이디는 8글자 이상 입력해야 됩니다");
        } else if (username.length() > 50) {
            errors.rejectValue("username", "아이디는 50글자 이하 입력해야 됩니다");
        }
        if (!Pattern.matches("^[a-z0-9]+$", username)) {
            errors.rejectValue("username", "아이디는 영문 소문자, 숫자만 입력해야 됩니다");
        }

        // name
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "이름은 필수입니다");
        if (name.length() > 50) {
            errors.rejectValue("name", "이름은 50글자 이하 입력해야 됩니다");
        }
        if (!Pattern.matches("^[가-힣]+$", name)) {
            errors.rejectValue("name", "이름은 한글만 입력해야 됩니다");
        }

        // nickname
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nickname", "닉네임은 필수입니다");
        if (nickname.length() > 50) {
            errors.rejectValue("nickname", "닉네임은 50글자 이하 입력해야 됩니다");
        }
        if (!Pattern.matches("^[0-9a-zA-Z가-힣]+$", nickname)) {
            errors.rejectValue("nickname", "닉네임은 영문, 한글, 숫자만 입력해야 됩니다");
        }
        
        // email
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "이메일은 필수입니다");
        if (!Pattern.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}+$", email)) {
            errors.rejectValue("email", "이메일 양식에 맞지 않습니다. 다시 작성해주세요");
        }

        // password
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password 는 필수입니다");
        if (password.length() < 8) {
            errors.rejectValue("password", "비밀번호는 8글자 이상 입력해야 됩니다");
        }
        // 입력 password, re_password 가 동일한지 비교
        if(!user.getPassword().equals(user.getRe_password())){
            errors.rejectValue("re_password", "비밀번호와 비밀번호 확인 입력값은 같아야 합니다");
        }

    }
}
