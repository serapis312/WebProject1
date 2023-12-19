package com.project.childprj.domain.mypage;

import com.project.childprj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class NickNameValidator implements Validator {

    public NickNameValidator(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return NickName.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NickName nickNameInstance = (NickName) target;

        String nickName = nickNameInstance.getNickName();

        // nickName
        if (userService.isExistByNickName(nickName)) {
            errors.rejectValue("nickName", "이미 존재하는 닉네임 입니다");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nickName", "닉네임은 필수입니다");
        if (nickName.length() > 50) {
            errors.rejectValue("nickName", "닉네임은 50글자 이하 입력해야 됩니다");
        }
        if (!Pattern.matches("^[0-9a-zA-Z가-힣]+$", nickName)) {
            errors.rejectValue("nickName", "닉네임은 영문, 한글, 숫자만 입력해야 됩니다");
        }

    }
}
