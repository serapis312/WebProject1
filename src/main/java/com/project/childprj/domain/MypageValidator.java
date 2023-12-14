package com.project.childprj.domain;

import com.project.childprj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class MypageValidator implements Validator {

    @Autowired
    UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        Long id = user.getId();
        String nickName = user.getNickName();
        String password = user.getPassword();

        // nickName
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nickName", "nickName 은 필수입니다");
        if (nickName.length() > 50) {
            errors.rejectValue("nickName", "닉네임은 50글자 이하 입력해야 됩니다");
        }
        if (!Pattern.matches("^[0-9a-zA-Z가-힣]+$", nickName)) {
            errors.rejectValue("nickName", "닉네임은 영문, 한글, 숫자만 입력해야 됩니다");
        }

        // password
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password 는 필수입니다");
        if (password.length() < 8) {
            errors.rejectValue("password", "비밀번호(password)는 8글자 이상 입력해야 됩니다");
        }
        // 입력 password, re_password 가 동일한지 비교
        if(!user.getPassword().equals(user.getRe_password())){
            errors.rejectValue("re_password", "비밀번호와 비밀번호 확인 입력값은 같아야 합니다");
        }
        // 회원 비밀번호와 입력한 비밀번호 일치여부 확인
        if (userService.findById(id) != null) {
            User originUser = userService.findById(id);
            if(!passwordEncoder.matches(password, originUser.getPassword())) {
                errors.rejectValue("password", "입력한 비밀번호는 회원 비밀번호와 일치해야 합니다");
            }
        }

    }
}
