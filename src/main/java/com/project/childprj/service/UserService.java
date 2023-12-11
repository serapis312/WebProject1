package com.project.childprj.service;

import com.project.childprj.domain.Authority;
import com.project.childprj.domain.User;

import java.util.List;

public interface UserService {
    // loginId(회원 아이디) 의 User 정보 읽어오기
    User findByLoginId(String loginId);

    // 특정 loginId(회원 아이디) 의 회원이 존재하는지 확인
    boolean isExist(String loginId);

    // 특정 email 의 회원이 존재하는지 확인
    boolean isExistByEmail(String email);

    // 특정 nickName 의 회원이 존재하는지 확인
    boolean isExistByNickName(String nickName);

    // 신규 회원 등록
    int register(User user);

    // 특정 사용자(id)의 authority(들)
    List<Authority> selectAuthoritiesById(Long id);

    // 아이디 찾기 (이메일 입력하여 찾기)
    String findLoginIdByEmail(String email);


    // 비밀번호 찾기는 비밀번호가 암호화 되어있어서 안된다... 비밀번호 변경으로 하는건 어떨까??

    // 비밀번호 찾기 (아이디로 찾기)
    String findPasswordByLoginId(String loginId);

    // 비밀번호 찾기 (이메일로 찾기)
    String findPasswordByEmail(String email);
}
