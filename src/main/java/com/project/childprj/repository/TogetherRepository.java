package com.project.childprj.repository;

import com.project.childprj.domain.Together;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TogetherRepository {
    void insertTogether(Together together);
    List<Together> selectAllTogether();
}
