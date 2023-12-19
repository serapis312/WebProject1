package com.project.childprj.controller;

import com.project.childprj.domain.Product;
import com.project.childprj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping("/list")
    public void marketList(){
    }
    @GetMapping("/detail/{id}")
    public String marketDetail(@PathVariable(name = "id") Long id, Model model){

        model.addAttribute("product", productService.productDetail(id)); // 특정 글
        return "/product/detail";
    }

    // 글 삭제
    @PostMapping("/detailDelete")
    public String detailDelete(Product product, Model model){

        Long productId = product.getId();
        model.addAttribute("change", productService.detailDelete(productId));
        return "/product/success";
    }

    @RequestMapping("/write")
    public void marketWrite(){
    }
    @RequestMapping("/update")
    public void marketUpdate(){
    }
}
