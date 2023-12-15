package com.project.childprj.domain.user;

import com.project.childprj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class NewPasswordValidator implements Validator {

    public NewPasswordValidator(PasswordEncoder passwordEncoder, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return NewPassword.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NewPassword newPasswordInstance = (NewPassword) target;

        String newPassword = newPasswordInstance.getNewPassword();
        String re_password  = newPasswordInstance.getRe_password();
        Long userId = newPasswordInstance.getUserId();

        // password
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword", "password 는 필수입니다");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "re_password", "password 확인은 필수입니다");
        if (newPassword.length() < 8) {
            errors.rejectValue("newPassword", "비밀번호(password)는 8글자 이상 입력해야 됩니다");
        }
        // 입력 password, re_password 가 동일한지 비교
        if(!newPassword.equals(re_password)){
            errors.rejectValue("re_password", "비밀번호와 비밀번호 확인 입력값은 같아야 합니다");
        }
        // 원래 비밀번호와 새로운 비밀번호가 동일하면 안됨
        if(userService.findById(userId) != null) {
            User originUser = userService.findById(userId);
            if(passwordEncoder.matches(newPassword, originUser.getPassword())) {
                errors.rejectValue("newPassword", "현재 비밀번호와 새로운 비밀번호가 똑같습니다");
            }
        }

    }
}
