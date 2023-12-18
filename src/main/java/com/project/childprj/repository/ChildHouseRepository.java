package com.project.childprj.repository;

import com.project.childprj.domain.ChildHouse;
import com.project.childprj.domain.Kindergarden;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildHouseRepository {

    void insertChildHouse(ChildHouse childHouse);
    List<ChildHouse> selectAllChildHouse();
}
