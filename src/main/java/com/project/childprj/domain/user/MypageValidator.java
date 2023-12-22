package com.project.childprj.domain.user;

import com.project.childprj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class MypageValidator implements Validator {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserImage.class.isAssignableFrom(clazz)
                || User.class.isAssignableFrom(clazz)
                || NickName.class.isAssignableFrom(clazz)
                || MypagePassword.class.isAssignableFrom(clazz)
                || CheckPassword.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if(target instanceof UserImage) {
            ValidationUtils.invokeValidator(new UserImageValidator(), target, errors);
        }
        if(target instanceof User) {
            ValidationUtils.invokeValidator(new UserValidator(userService), target, errors);
        }
        if(target instanceof NickName) {
            ValidationUtils.invokeValidator(new NickNameValidator(userService), target, errors);
        }
        if(target instanceof CheckPassword) {
            ValidationUtils.invokeValidator(new CheckPasswordValidator(passwordEncoder, userService), target, errors);
        }
        if(target instanceof MypagePassword) {
            ValidationUtils.invokeValidator(new MypagePasswordValidator(passwordEncoder, userService), target, errors);
        }
    }
}
