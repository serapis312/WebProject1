package com.project.childprj.repository;

import com.project.childprj.domain.Authority;
import com.project.childprj.domain.User;

import java.util.List;

public interface AuthorityRepository {
    // 특정 이름(authName) 의 권한 정보 읽어오기
    Authority findByAuthName(String authName);

    // 특정 사용자(User) 의 권한(들) 읽어오기
    List<Authority> findByUser(User user);

    // 특정 사용자(user_id) 에 권한(auth_id) 추가 (INSERT)
    int addAuthority(Long userId, Long authId);
}
