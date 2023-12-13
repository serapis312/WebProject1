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

    // 특정 name 과 email 의 user 리턴
    User findByNameAndEmail(String name, String email);

    // 특정 name 과 loginId 의 user 리턴
    User findByNameAndLoginId(String name, String loginId);

    // findByNameAndEmail, findByNameAndLoginId 로 찾은 해당 user 비밀번호 변경
    int updatePassword(User user);

    // 새로운 User 등록
    int save(User user);

    // User 정보 수정
    int update(User user);

    // 특정 user 삭제
    int delete(User user);

}
