package com.project.childprj.controller;

import com.project.childprj.domain.ChildHouse;
import com.project.childprj.repository.ChildHouseRepository;
import com.project.childprj.service.ProtectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/protect")
public class ProtectController {

    @Autowired
    private ProtectService protectService;

    @Autowired
    private ChildHouseRepository childHouseRepository;

    @GetMapping("/childhouse")
    public String getChildHouses(Model model) {
        List<ChildHouse> childHouses = childHouseRepository.findAll();
        model.addAttribute("childHouses", childHouses);
        return "childHouseList";
    }

//    @GetMapping("/kindergardens")
//    public String getKindergartens(Model model) {
//        List<Kindergarten> kindergartens = kindergartenRepository.findAll();
//        model.addAttribute("kindergartens", kindergartens);
//        return "kindergartenList";
//    }
}
