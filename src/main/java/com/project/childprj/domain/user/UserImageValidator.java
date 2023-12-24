package com.project.childprj.domain.user;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserImageValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return UserImage.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserImage userImage = (UserImage) target;

    }
}
