package com.project.childprj.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.project.childprj.domain.Kindergarden;
import com.project.childprj.util.Utils;

@Service
public class KindergardenService {
	
	private final SqlSession sqlSession;

	@Value("${app.api.kinderKey}")
	private String kinderKey;

	@Autowired
	public KindergardenService(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Transactional
	public void insertKindergarden(Kindergarden kindergarden) {
		sqlSession.insert("insertKindergarden", kindergarden);
	}
	
	// 유치원 목록 조회 API 연동 서비스
	public List<Kindergarden> getKindergarden(Integer startIndex, Integer endIndex) throws JsonProcessingException {
		String type = "json"; // 요청 파일 타입
		String service = "childSchoolInfo"; // 서비스명

		String uri = String.format("http://openapi.seoul.go.kr:8088/%s/%s/%s/%d/%d",
				kinderKey, type, service, startIndex, endIndex);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
		List<Kindergarden> result = new ArrayList<>();

		if (response.getStatusCode().is2xxSuccessful()) {
			String jsonData = response.getBody();
			if (jsonData != null && jsonData.length() > 0) {
				JsonNode rootNode = Utils.jsonToJsonNode(jsonData);
				// KindergardenInfo > row 데이터를 꺼냄
				ArrayNode rows = (ArrayNode) rootNode.get("KindergardenInfo").get("row");
				for (JsonNode row : rows) {
					Kindergarden kindergarden = Kindergarden.fromJson(row);
					result.add(kindergarden);


					// DB 저장
					// KindergardenMapper.xml에서 id 제거, ODATE 값 NOW()로 변경
					// tables_create.sql DDL문에 id AUTO_INCREMENT 추가
//					this.insertKindergarden(kindergarden);
				}
			}
		}

		return result;
	}

	public List<Kindergarden> getAllKindergarden() {
		return sqlSession.selectList("selectAllKindergarden");
	}
}
