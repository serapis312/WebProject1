package com.project.childprj.repository;

import com.project.childprj.domain.User;

public interface UserRepository {
    // 특정 id (PK) 의 user 리턴
    User findById(Long id);


    // 특정 loginId 의 user 리턴
    User findByLoginId(String loginId);


    // 특정 email 의 user 리턴
    User findByEmail(String email);

    // 특정 nickName 의 user 리턴
    User findByNickName(String nickName);


    // 새로운 User 등록
    int save(User user);


    // User 정보 수정
    int update(User user);
}
