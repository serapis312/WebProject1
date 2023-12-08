package com.project.childprj.service;

import com.project.childprj.domain.ChildHouse;

import java.util.List;

public interface ProtectService {

    // 기본 crud 작업
    List<ChildHouse> getAllChildHouses();
    ChildHouse getChildHouseById(Long id);
    ChildHouse saveChildHouse(ChildHouse childHouse);
    void deleteChildHouse(Long id);
    
}
