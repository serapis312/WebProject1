package com.project.childprj.domain.user;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class NameAndLoginIdValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return NameAndLoginId.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NameAndLoginId nameAndLoginId = (NameAndLoginId) target;

        String name = nameAndLoginId.getName();
        String loginId = nameAndLoginId.getLoginId();

        // name
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name 은 필수입니다");
        if (name.length() > 50) {
            errors.rejectValue("name", "이름은 50글자 이하 입력해야 됩니다");
        }
        if (!Pattern.matches("^[가-힣]+$", name)) {
            errors.rejectValue("name", "이름은 한글만 입력해야 됩니다");
        }

        // loginId
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "loginId", "loginId 는 필수입니다");
        if (loginId.length() < 8) {
            errors.rejectValue("loginId", "아이디(loginId)는 8글자 이상 입력해야 됩니다");
        } else if (loginId.length() > 50) {
            errors.rejectValue("loginId", "아이디(loginId)는 50글자 이하 입력해야 됩니다");
        }
        if (!Pattern.matches("^[a-z0-9]+$", loginId)) {
            errors.rejectValue("loginId", "아이디(loginId)는 영문 소문자, 숫자만 입력해야 됩니다");
        }

    }
}
