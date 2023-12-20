package com.project.childprj.service;


import com.project.childprj.domain.Product;
import com.project.childprj.domain.User;
import com.project.childprj.repository.ProductRepository;
import com.project.childprj.util.U;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(SqlSession sqlSession) {
        productRepository = sqlSession.getMapper(ProductRepository.class);
    }

    // 글 목록 조회
    @Override
    public List<Product> list(Integer page, String searchTxt, Model model) {
        HttpSession session = U.getSession();

        if (page == null) page = 1;
        if (page < 1) page = 1;

        if (searchTxt == null) searchTxt = "";

        String orderWay = (String) session.getAttribute("orderWay");
        if (orderWay == null) orderWay = "최신순";

        Integer pagesPerSection = 5;
        Integer rowsPerPage = 8;

        int totalLength = productRepository.selectCountAll(searchTxt);
        int totalPage = (int) Math.ceil(totalLength / (double) rowsPerPage);

        int startPage = 0;
        int endPage = 0;

        List<Product> products = null;

        if (totalLength > 0) {
            if (page > totalPage) page = totalPage;

            int fromRow = (page - 1) * rowsPerPage;

            startPage = (((page - 1) / pagesPerSection) * pagesPerSection) + 1;
            endPage = startPage + pagesPerSection - 1;
            if (endPage > totalPage) endPage = totalPage;

            if (orderWay.equals("최신순")) {
                products = productRepository.selectFromCntOrderByDate(fromRow, rowsPerPage, searchTxt);
            } else if (orderWay.equals("가격순")) {
                products = productRepository.selectFromCntOrderByPrice(fromRow, rowsPerPage, searchTxt);
            }
            model.addAttribute("products", products);
        } else {
            page = 0;
        }

        model.addAttribute("page", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("rowsPerPage", rowsPerPage);
        model.addAttribute("orderWay", orderWay);
        model.addAttribute("searchTxt", searchTxt);

        model.addAttribute("url", U.getRequest().getRequestURI());
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return products;
    }

    // 글 작성
    public int write(Product product) {
        LocalDateTime dateTime = LocalDateTime.now();
        User user = new User(2L, "apple1234", "1234", "맑은 아침햇살", "김멜론", "mellon@mail.com", dateTime);

        product.setUser(user);
        int cnt = productRepository.insert(product);

        return cnt;
    }

    // 특정 글 가져오기
    @Override
    public Product productDetail(Long id) {
        productRepository.incViewCnt(id);
        return productRepository.findProductById(id);
    }

    // 특정 글 삭제
    @Override
    public int detailDelete(Long id) {
        return productRepository.detailDelete(id);
    }
}
