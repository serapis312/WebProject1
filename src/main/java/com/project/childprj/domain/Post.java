package com.project.childprj.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post implements Validator {
    private Long id;
    private String name;
    private String title;
    private String content;
    private int viewCnt;
    private Date createDate;

    @Override
    public boolean supports(Class<?> clazz) {
        System.out.println("supports(" + clazz.getName() + ") 호출");

        boolean result = com.project.childprj.domain.Post.class.isAssignableFrom(clazz);
        System.out.println(result);
        return result;
    }

    @Override
    public void validate(Object target, Errors errors) {
        com.project.childprj.domain.Post post = (Post) target;

        System.out.println("validate() 호출 : " + post);

        String user = post.getName();
        if(user == null || user.trim().isEmpty()){
            errors.rejectValue("user", "작성자는 필수입니다");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "subject", "글 제목은 필수입니다");
    }
}
