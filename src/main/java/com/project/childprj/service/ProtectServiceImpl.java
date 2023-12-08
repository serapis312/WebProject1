package com.project.childprj.service;

import com.project.childprj.domain.ChildHouse;
import com.project.childprj.repository.ChildHouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProtectServiceImpl implements ProtectService{
    @Autowired
    private ChildHouseRepository childHouseRepository;

    // todo: 킨더가든 레포지토리 작성


    @Override
    public List<ChildHouse> getAllChildHouses(){
        return childHouseRepository.findAll();
    }

    @Override
    public ChildHouse getChildHouseById(Long id){
        return childHouseRepository.findById(id).orElse(null);
    }

    @Override
    public ChildHouse saveChildHouse(ChildHouse childHouse) {
        return childHouseRepository.save(childHouse);
    }

    @Override
    public void deleteChildHouse(Long id) {
        childHouseRepository.deleteById(id);
    }

}
