package com.project.childprj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.project.childprj.domain.Together;
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
public class TogetherService {
    private static SqlSession sqlSession;

        @Value("${app.api.SeoulDataKey}")
        private static String SeoulDataKey;

        @Autowired
        public TogetherService(SqlSession sqlSession) {
            this.sqlSession = sqlSession;
        }

        @Transactional
        public int insertTogether(Together together) {
             return sqlSession.insert("insertTogether", together);
        }

        // 유치원 목록 조회 API 연동 서비스
        public List<Together> getTogether(Integer startIndex, Integer endIndex) throws JsonProcessingException {
            String type = "json"; // 요청 파일 타입
            String service = "culturalEventInfo"; // 서비스명

            String uri = String.format("http://openapi.seoul.go.kr:8088/%s/%s/%s/%d/%d",
                    SeoulDataKey, type, service, startIndex, endIndex);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
            List<Together> result = new ArrayList<>();

            if (response.getStatusCode().is2xxSuccessful()) {
                String jsonData = response.getBody();
                if (jsonData != null && jsonData.length() > 0) {
                    JsonNode rootNode = Utils.jsonToJsonNode(jsonData);
                    // TogetherInfo > row 데이터를 꺼냄
                    ArrayNode rows = (ArrayNode) rootNode.get("culturalEventInfo").get("row");
                    for (JsonNode row : rows) {
                        Together together = Together.fromJson(row);
                        result.add(together);


                        // DB 저장
                        // KindergardenMapper.xml에서 id 제거, ODATE 값 NOW()로 변경
                        // tables_create.sql DDL문에 id AUTO_INCREMENT 추가
					 this.insertTogether(together);
                    }
                }
            }

            return result;
        }
    public Together getTogether(String codename) {
        return this.sqlSession.selectOne("selectTogether", codename);
    }

        public List<Together> getAllTogether() {
            return sqlSession.selectList("selectAllTogether");
        }
    }


