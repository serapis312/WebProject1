package com.project.childprj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.project.childprj.domain.ChildHouse;
import com.project.childprj.util.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChildHouseService {
    private final SqlSession sqlSession;

    @Value("${app.api.kinderKey}")
    private String childHouseKey;

    @Autowired
    public ChildHouseService(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Transactional
    public void insertChildHouse(ChildHouse childHouse) {

        sqlSession.insert("insertChildHouse", childHouse);
    }

    public List<ChildHouse> getChildHouse(Integer startIndex, Integer endIndex) throws JsonProcessingException {
        String type = "json"; // 요청 파일 타입
        String service = "ChildCareInfo"; // 서비스명

        String uri = String.format("http://openapi.seoul.go.kr:8088/%s/%s/%s/%d/%d",
                childHouseKey, type, service, startIndex, endIndex);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        List<ChildHouse> result = new ArrayList<>();

        if (response.getStatusCode().is2xxSuccessful()) {
            String jsonData = response.getBody();
            if (jsonData != null && jsonData.length() > 0) {
                JsonNode rootNode = Utils.jsonToJsonNode(jsonData);
                // ChildCareInfoInfo > row 데이터를 꺼냄
                ArrayNode rows = (ArrayNode) rootNode.get("ChildCareInfo").get("row");
                for (JsonNode row : rows) {
                    ChildHouse childHouse = ChildHouse.fromJson(row);
                    result.add(childHouse);


                    // DB 저장
                    // KindergardenMapper.xml에서 id 제거, ODATE 값 NOW()로 변경
                    // tables_create.sql DDL문에 id AUTO_INCREMENT 추가
					this.insertChildHouse(childHouse);
                }
            }
        }

        return result;
    }

    public ChildHouse getChildHouse(String crName){
        return this.sqlSession.selectOne("selectChildHouse", crName);
    }

    public List<ChildHouse> getAllChildHouse() {
        return sqlSession.selectList("selectAllChildHouse");
    }


}
