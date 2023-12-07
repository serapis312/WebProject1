package com.project.childprj.controller;

import com.project.childprj.domain.ChildHouse;
import com.project.childprj.repository.ChildHouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/protect")
public class ChildHouseController {

    @Autowired
    private ChildHouseRepository childHouseRepository;

    @GetMapping("/childhouse")
    public String getChildHouses(Model model) {
        List<ChildHouse> childHouses = childHouseRepository.findAll();
        model.addAttribute("childHouses", childHouses);
        return "childHouseList";
    }

//    @GetMapping("/kindergartens")
//    public String getKindergartens(Model model) {
//        List<Kindergarten> kindergartens = kindergartenRepository.findAll();
//        model.addAttribute("kindergartens", kindergartens);
//        return "kindergartenList"; // Thymeleaf 템플릿의 이름 (kindergartenList.html)
//    }
}
