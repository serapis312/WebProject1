package com.project.childprj.domain.user;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class NameAndUsernameValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return NameAndUsername.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NameAndUsername nameAndUsername = (NameAndUsername) target;

        String name = nameAndUsername.getName();
        String username = nameAndUsername.getUsername();

        // name
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "이름은 필수입니다");
        if (name.length() > 50) {
            errors.rejectValue("name", "이름은 50글자 이하 입력해야 됩니다");
        }
        if (!Pattern.matches("^[가-힣]+$", name)) {
            errors.rejectValue("name", "이름은 한글만 입력해야 됩니다");
        }

        // username
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username 는 필수입니다");
        if (username.length() < 8) {
            errors.rejectValue("username", "아이디는 8글자 이상 입력해야 됩니다");
        } else if (username.length() > 50) {
            errors.rejectValue("username", "아이디는 50글자 이하 입력해야 됩니다");
        }
        if (!Pattern.matches("^[a-z0-9]+$", username)) {
            errors.rejectValue("username", "아이디는 영문 소문자, 숫자만 입력해야 됩니다");
        }

    }
}
