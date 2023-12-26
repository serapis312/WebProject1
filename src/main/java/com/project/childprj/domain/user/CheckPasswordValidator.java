package com.project.childprj.domain.user;

import com.project.childprj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class CheckPasswordValidator implements Validator {

    public CheckPasswordValidator(PasswordEncoder passwordEncoder, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Autowired
    UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean supports(Class<?> clazz) {
        return CheckPassword.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CheckPassword checkPassword = (CheckPassword) target;

        String originPassword = checkPassword.getOriginPassword();
        String re_password = checkPassword.getRe_password();
        Long user_id = checkPassword.getUser_id();

        // password
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "originPassword", "비밀번호는 필수입니다");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "re_password", "비밀번호확인은 필수입니다");
        if (originPassword.length() < 8) {
            errors.rejectValue("originPassword", "비밀번호는 8글자 이상 입력해야 됩니다");
        }
        // 입력 password, re_password 가 동일한지 비교
        if (!originPassword.equals(re_password)) {
            errors.rejectValue("re_password", "비밀번호와 비밀번호 확인 입력값은 같아야 합니다");
        }
        // 회원 비밀번호와 입력한 비밀번호 일치여부 확인
        if (userService.findById(user_id) != null) {
            User originUser = userService.findById(user_id);
            if (!passwordEncoder.matches(originPassword, originUser.getPassword())) {
                errors.rejectValue("originPassword", "입력한 비밀번호는 회원 비밀번호와 일치해야 합니다");
            }
        }
    }
}
