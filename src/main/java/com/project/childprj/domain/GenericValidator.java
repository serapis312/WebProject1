package com.project.childprj.domain;

import com.project.childprj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class GenericValidator implements Validator {

    @Autowired
    UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz)
                || NameAndEmail.class.isAssignableFrom(clazz)
                || NameAndLoginId.class.isAssignableFrom(clazz)
                || Password.class.isAssignableFrom(clazz);
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
        if(target instanceof Password) {
            ValidationUtils.invokeValidator(new PasswordValidator(), target, errors);
        }

    }
}
