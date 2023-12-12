package com.project.childprj.service;

import com.project.childprj.domain.Kindergarden;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class KindergardenService {

    private final SqlSession sqlSession;

    @Autowired
    public KindergardenService(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Transactional
    public void insertKindergarden(Kindergarden kindergarden){
        sqlSession.insert("insertKindergarden", kindergarden);
    }

    public List<Kindergarden> getAllKindergarden() {
        return sqlSession.selectList("selectAllKindergarden");
    }
}
