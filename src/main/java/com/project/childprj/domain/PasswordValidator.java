package com.project.childprj.domain;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class PasswordValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Password.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Password password = (Password) target;

        String newPassword = password.getNewPassword();
        String re_password  = password.getRe_password();

        // password
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword", "password 는 필수입니다");
        if (newPassword.length() < 8) {
            errors.rejectValue("newPassword", "비밀번호(password)는 8글자 이상 입력해야 됩니다");
        }
        // 입력 password, re_password 가 동일한지 비교
        if(!newPassword.equals(re_password)){
            errors.rejectValue("re_password", "비밀번호와 비밀번호 확인 입력값은 같아야 합니다");
        }

    }
}
