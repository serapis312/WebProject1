package com.project.childprj.domain.user;

import com.project.childprj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class GenericValidator implements Validator {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz)
                || NameAndEmail.class.isAssignableFrom(clazz)
                || NameAndLoginId.class.isAssignableFrom(clazz)
                || NewPassword.class.isAssignableFrom(clazz)
                || UserImage.class.isAssignableFrom(clazz)
                || NickName.class.isAssignableFrom(clazz)
                || MypagePassword.class.isAssignableFrom(clazz)
                || CheckPassword.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        if(target instanceof User) {
            ValidationUtils.invokeValidator(new UserValidator(userService), target, errors);
        }
        if(target instanceof NameAndEmail) {
            ValidationUtils.invokeValidator(new NameAndEmailValidator(), target, errors);
        }
        if(target instanceof NameAndLoginId) {
            ValidationUtils.invokeValidator(new NameAndLoginIdValidator(), target, errors);
        }
        if(target instanceof NewPassword) {
            ValidationUtils.invokeValidator(new NewPasswordValidator(passwordEncoder, userService), target, errors);
        }
        if(target instanceof UserImage) {
            ValidationUtils.invokeValidator(new UserImageValidator(), target, errors);
        }
        if(target instanceof NickName) {
            ValidationUtils.invokeValidator(new NickNameValidator(userService), target, errors);
        }
        if(target instanceof MypagePassword) {
            ValidationUtils.invokeValidator(new MypagePasswordValidator(passwordEncoder, userService), target, errors);
        }
        if(target instanceof CheckPassword) {
            ValidationUtils.invokeValidator(new CheckPasswordValidator(passwordEncoder, userService), target, errors);
        }

    }
}
