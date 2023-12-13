package com.project.childprj.domain;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class NameAndEmailValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return NameAndEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NameAndEmail nameAndEmail = (NameAndEmail) target;

        String name = nameAndEmail.getName();
        String email = nameAndEmail.getEmail();

        // name
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name 은 필수입니다");
        if (name.length() > 50) {
            errors.rejectValue("name", "이름은 50글자 이하 입력해야 됩니다");
        }
        if (!Pattern.matches("^[가-힣]+$", name)) {
            errors.rejectValue("name", "이름은 한글만 입력해야 됩니다");
        }

        // email
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email 은 필수입니다");
        if (!Pattern.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}+$", email)) {
            errors.rejectValue("email", "이메일 양식에 맞지 않습니다. 다시 작성해주세요");
        }

    }
}
