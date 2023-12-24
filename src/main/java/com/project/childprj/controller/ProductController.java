package com.project.childprj.controller;

import com.project.childprj.domain.Product;
import com.project.childprj.domain.ProductValidator;
import com.project.childprj.service.ProductService;
import com.project.childprj.util.U;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    // 글 목록 조회
    @GetMapping("/list")
    public void list(Integer page, String searchTxt, Model model) {
        productService.list(page, searchTxt, model);
    }

    @PostMapping("/orderWay")
    public String orderWay(String orderWay, String searchTxt, RedirectAttributes redirectAttrs) {
        U.getSession().setAttribute("orderWay", orderWay);
        redirectAttrs.addAttribute("searchTxt", searchTxt);
        return "redirect:/product/list";
    }

    @PostMapping("/search")
    public String search(String searchTxt, RedirectAttributes redirectAttrs) {
        redirectAttrs.addAttribute("searchTxt", searchTxt);
        return "redirect:/product/list";
    }

    // 글 작성
    @GetMapping("/write")
    public void write() {
    }

    @PostMapping("/write")
    public String writeOk(
            @Valid Product product
            , BindingResult result
            , Model model
            , RedirectAttributes redirectAttrs
    ) {
        if (result.hasErrors()) {
            redirectAttrs.addFlashAttribute("price", product.getPrice());
            redirectAttrs.addFlashAttribute("productName", product.getProductName());
            redirectAttrs.addFlashAttribute("region", product.getRegion());
            redirectAttrs.addFlashAttribute("content", product.getContent());

            List<FieldError> errList = result.getFieldErrors();
            for (FieldError err : errList) {
                redirectAttrs.addFlashAttribute("err_" + err.getField(), err.getCode());
            }

            return "redirect:/product/write";
        }

        model.addAttribute("result", productService.write(product));
        return "product/writeOk";
    }

    @GetMapping("/detail/{id}")
    public String marketDetail(@PathVariable(name = "id") Long id, Model model){
        model.addAttribute("product", productService.productDetail(id)); // 특정 글
        return "product/detail";
    }

    // 글 삭제
    @PostMapping("/detailDelete")
    public String detailDelete(Product product, Model model){
        Long productId = product.getId();
        model.addAttribute("change", productService.detailDelete(productId));
        return "product/success";
    }

    // 글 수정
    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        Product product = productService.productDetail(id);
        model.addAttribute("product", product);
        return "product/update";
    }

    @PostMapping("/update")
    public String updateOk(
            @Valid Product product
            , BindingResult result
            , Model model
            , RedirectAttributes redirectAttrs
    ) {
        if (result.hasErrors()) {
            redirectAttrs.addFlashAttribute("price", product.getPrice());
            redirectAttrs.addFlashAttribute("productName", product.getProductName());
            redirectAttrs.addFlashAttribute("region", product.getRegion());
            redirectAttrs.addFlashAttribute("content", product.getContent());

            List<FieldError> errList = result.getFieldErrors();
            for (FieldError err : errList) {
                redirectAttrs.addFlashAttribute("err_" + err.getField(), err.getCode());
            }

            return "redirect:/product/update" + product.getId();
        }

        model.addAttribute("result", productService.update(product));
        return "product/updateOk";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(new ProductValidator());
    }
}
