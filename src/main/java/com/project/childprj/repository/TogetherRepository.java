package com.project.childprj.repository;

import com.project.childprj.domain.Together;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TogetherRepository {


    void insertTogether(Together together);
    List<Together> selectAllTogether();


    int deleteAll();


    // 다른 메서드들을 추가할 수 있음


}
