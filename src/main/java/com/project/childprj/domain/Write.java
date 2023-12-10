package com.project.childprj.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Write implements Validator {
    int id;
    String name;
    String title;

    @Override
    public boolean supports(Class<?> clazz) {
        return Write.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        System.out.println("validate() 호출");
        Write write = (Write) target;

        String name = write.getName();

        if(name == null || name.trim().isEmpty()){
            errors.rejectValue("name", "name 은 필수입니다");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "title은 필수입니다");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "content", "content는 필수입니다");
    }
}



