package com.project.childprj.repository;

import com.project.childprj.domain.Kindergarden;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KindergardenRepository {
    void insertKindergarden(Kindergarden kindergarden);
    List<Kindergarden> selectAllKindergarden();
}