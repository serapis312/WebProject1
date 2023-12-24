package com.project.childprj.domain;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ProductValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        boolean result = Product.class.isAssignableFrom(clazz);
        return result;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Product product = (Product) target;

        if (product.getPrice() == null) {
            errors.rejectValue("price", "가격은 필수입니다");
        } else {
            System.out.println(((Object)product.getPrice()).getClass().getSimpleName().equals("Integer"));
        }

//        else if (!((Object)product.getPrice()).getClass().getSimpleName().equals("Integer")) {
//            errors.rejectValue("price", "나눔은 0을 입력해주세요");
//        }

        if (product.getProductName() == null || product.getProductName().trim().isEmpty()) {
            errors.rejectValue("productName", "상품명은 필수입니다");
        }

        if (product.getRegion() == null || product.getRegion().trim().isEmpty()) {
            errors.rejectValue("region", "지역은 필수입니다");
        }

        if (product.getContent() == null || product.getContent().trim().isEmpty()) {
            errors.rejectValue("content", "내용은 필수입니다");
        }
    }
}
