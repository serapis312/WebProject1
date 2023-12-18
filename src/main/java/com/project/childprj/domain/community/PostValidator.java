package com.project.childprj.domain.community;


import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class PostValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Post.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Post post = (Post) target;

        String title = post.getTitle();

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "글 제목은 필수입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "content", "글 내용은 필수입니다.");
        if(title.length() > 50) {
            errors.rejectValue("title", "글 제목은 50자 이하입니다");
        }

    }
}
