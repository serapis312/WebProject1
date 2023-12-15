package com.project.childprj.domain.user;

import com.project.childprj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class MypagePasswordValidator implements Validator {

    public MypagePasswordValidator(PasswordEncoder passwordEncoder, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Autowired
    UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean supports(Class<?> clazz) {
        return MypagePassword.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MypagePassword mypagePassword = (MypagePassword) target;

        String nowPassword = mypagePassword.getNowPassword();
        String newPassword = mypagePassword.getNewPassword();
        String re_password = mypagePassword.getRe_password();
        Long userId = mypagePassword.getUserId();


        // password
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nowPassword", "현재 비밀번호 는 필수입니다");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword", "새로운 비밀번호 는 필수입니다");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "re_password", "비밀번호 확인은 필수입니다");
        if (newPassword.length() < 8) {
            errors.rejectValue("newPassword", "비밀번호(password)는 8글자 이상 입력해야 됩니다");
        }
        // 입력 password, re_password 가 동일한지 비교
        if(!newPassword.equals(re_password)){
            errors.rejectValue("re_password", "비밀번호와 비밀번호 확인 입력값은 같아야 합니다");
        }
        // 현재 사용자가 존재하면
        if(userService.isExistById(userId)) {

            // 원래 비밀번호와 새로운 비밀번호가 동일하면 안됨
            User originUser = userService.findById(userId);
            if(passwordEncoder.matches(newPassword, originUser.getPassword())) {
                errors.rejectValue("newPassword", "현재 비밀번호와 새로운 비밀번호가 똑같습니다");
            }

            // 회원 비밀번호와 입력한 비밀번호 일치여부 확인
            if (!passwordEncoder.matches(nowPassword, originUser.getPassword())) {
                errors.rejectValue("nowPassword", "입력한 비밀번호는 회원 비밀번호와 일치해야 합니다");
            }
        }

    }
}
