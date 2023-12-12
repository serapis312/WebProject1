package com.project.childprj.controller;

import com.project.childprj.domain.Kindergarden;
import com.project.childprj.service.KindergardenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/protect")
public class KindergardenController {

    private final KindergardenService kindergardenService;

    @Autowired
    public KindergardenController(KindergardenService kindergardenService) {
        this.kindergardenService = kindergardenService;
    }

    @Value("${app.api.kinderKey}")
    private String kinderKey;


    @GetMapping("/api/kindergarden/{start_index}/{end_index}")
    public ResponseEntity<String> kindergarden(
            @PathVariable("start_index") Integer startIndex,
            @PathVariable("end_index") Integer endIndex
    ) {
        String type = "json"; // 요청 파일 타입
        String service = "childSchoolInfo"; // 서비스명

        String uri = String.format("http://openapi.seoul.go.kr:8088/%s/%s/%s/%d/%d",
                kinderKey, type, service, startIndex, endIndex);

        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.getForEntity(uri, String.class);

        return response;
    }

    // Thymeleaf 사용해서 list 보여줌
    @GetMapping("/api/kindergarden/{start_index}/{end_index}/form/kindergarden")
    public String showList(Model model) {
        // 여기에서 유치원 목록을 가져오는 서비스 메서드를 호출하고 모델에 추가하는 로직 추가
        List<Kindergarden> kindergarden = kindergardenService.getAllKindergarden();
        model.addAttribute("kindergarden", kindergarden);

        return "kindergardenList";
    }

    @GetMapping("/save/kindergarden")
    public String showSaveKindergardenForm() {
        return "saveKindergardenForm";
    }

    @PostMapping("/save/api/kindergarden")
    public String insertKindergarden(
            @RequestParam(name = "kinderName", required = false) String kinderName,
            @RequestParam(name = "establish", required = false) String establish,
            @RequestParam(name = "ldgrName", required = false) String ldgrName,
            @RequestParam(name = "oDate", required = false) String oDate,
            @RequestParam(name = "addr", required = false) String addr,
            @RequestParam(name = "telNo", required = false) String telNo,
            @RequestParam(name = "hpAddr", required = false) String hpAddr,
            @RequestParam(name = "operTime", required = false) String operTime
    ) {
        Kindergarden kindergarden = new Kindergarden();
        kindergarden.setKINDERNAME(kinderName);
        kindergarden.setESTABLISH(establish);
        kindergarden.setLDGRNAME(ldgrName);
        kindergarden.setADDR(addr);
        kindergarden.setTELNO(telNo);
        kindergarden.setHPADDR(hpAddr);
        kindergarden.setOPERTIME(operTime);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (dateFormat != null && oDate != null) {
            try {
                Date date = dateFormat.parse(oDate);
                kindergarden.setODATE(date);
            } catch (ParseException e) {
                e.printStackTrace();
                // 날짜 파싱 실패 처리
            }
        } else {
            // dateFormat 또는 oDate가 null인 경우에 대한 처리
        }
        kindergardenService.insertKindergarden(kindergarden);
        return "redirect:/index";
    }
}

//@Controller
//@RequestMapping("/protect")
//public class KindergardenController {
//
//    private final KindergardenService kindergardenService;
//
//    @Autowired
//    public KindergardenController(KindergardenService kindergardenService) {
//        this.kindergardenService = kindergardenService;
//    }
//
//    @Value("${app.api.kinderKey}")
//    private String kinderKey;
//
//    @GetMapping("/api/kindergarden/{start_index}/{end_index}")
//    public ResponseEntity<String> kindergarden(
//            @PathVariable("start_index") Integer startIndex,
//            @PathVariable("end_index") Integer endIndex
//    ) {
//        String type = "json"; // 요청 파일 타입
//        String service = "childSchoolInfo"; // 서비스명
//
//        String uri = String.format("http://openapi.seoul.go.kr:8088/%s/%s/%s/%d/%d",
//                kinderKey, type, service, startIndex, endIndex);
//
//        RestTemplate rt = new RestTemplate();
//        ResponseEntity<String> response = rt.getForEntity(uri, String.class);
//
//        return response;
//    }
//
//    // Thymeleaf 사용해서 list 보여줌
//    @GetMapping("/form/kindergarden")
//    public String showList(){
//        return "kindergardenList";
//    }
//
//    @PostMapping("/save/kindergarden")
//    public String saveKindergarden(
//        @RequestParam("name") String name,
//        @RequestParam("establish") String establish,
//        @RequestParam("ldgrName") String ldgrName,
//        @RequestParam("oDate") String oDate,
//        @RequestParam("addr") String addr,
//        @RequestParam("telNo") String telNo,
//        @RequestParam("hpAddr") String hpAddr,
//        @RequestParam("operTime") String operTime,
//        Model model
//    ){
//        Kindergarden kindergarden = new Kindergarden();
//        kindergarden.setKINDERNAME(name);
//        kindergarden.setESTABLISH(establish);
//        kindergarden.setLDGRNAME(ldgrName);
//        kindergarden.setADDR(addr);
//        kindergarden.setTELNO(telNo);
//        kindergarden.setHPADDR(hpAddr);
//        kindergarden.setOPERTIME(operTime);
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            Date date = dateFormat.parse(oDate);
//            kindergarden.setODATE(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//            // 날짜 파싱 실패 처리
//        }
//
//        kindergardenService.saveKindergarden(kindergarden);
//        return "redirect:/protect/kindergardenList";
//    }
//
////    public String showKindergardenList(Model model) {
////        // 여기에서 유치원 목록을 가져오는 서비스 메서드를 호출하고 모델에 추가하는 로직 추가
////        List<Kindergarden> kindergardens = kindergardenService.getAllKindergardens();
////        model.addAttribute("kindergardens", kindergardens);
////
////        return "kindergardenList";
////    }
//    @Transactional
//    public void saveKindergarden(Kindergarden kindergarden){
//        sqlSession.insert("insertKindergarden", kindergarden);
//    }
//
//    public List<Kindergarden> getAllKindergardens(Kindergarden kindergarden) {
//        // MyBatis를 사용하여 데이터베이스에서 유치원 목록을 가져오는 쿼리 실행
//        return sqlSession.selectList("selectAllKindergardens", kindergarden);
//    }
//}
//
