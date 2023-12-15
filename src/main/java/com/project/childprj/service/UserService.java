package com.project.childprj.service;

import com.project.childprj.domain.mypage.NickName;
import com.project.childprj.domain.mypage.UserImage;
import com.project.childprj.domain.user.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    // 특정 Id 의 user 정보 읽어오기
    User findById(Long id);

    // 특정 loginId(회원 아이디) 의 User 정보 읽어오기
    User findByLoginId(String loginId);

    // 특정 email(이메일) 의 user 정보 읽어오기
    User findByEmail(String email);

    // 특정 loginId(회원 아이디) 의 회원이 존재하는지 확인
    boolean isExist(String loginId);

    // 특정 id(PK) 의 회원이 존재하는지 확인
    boolean isExistById(Long id);

    // 특정 email 의 회원이 존재하는지 확인
    boolean isExistByEmail(String email);

    // 특정 nickName 의 회원이 존재하는지 확인
    boolean isExistByNickName(String nickName);

    // 신규 회원 등록
    int register(User user);

    // 마이페이지 프로필사진 업로드
    int uploadProfile(MultipartFile file, Long userId);

    // 마이페이지 userImage 정보 읽어오기
    UserImage findUserImage(Long userId);

    // 마이페이지 닉네임 수정
    int updateNickName(NickName nickName);

    // 특정 사용자(id)의 authority(들)
    List<Authority> selectAuthoritiesById(Long id);

    // 아이디 찾기 (이름, 이메일 입력하여 찾기)
    String findLoginIdByNameAndEmail(NameAndEmail nameAndEmail);

    // 비밀번호 찾기는 비밀번호가 암호화 되어있어서 안된다... 비밀번호 변경으로 하는건 어떨까??
    // 1. 이름, 아이디로 비밀번호 변경
    // 2. 이름, 이메일로 비밀번호 변경

    // 해당 이름, 아이디의 사용자가 존재하는지 확인
    boolean isExistByNameAndLoginId(NameAndLoginId nameAndLoginId);

    // 해당 이름, 이메일의 사용자가 존재하는지 확인
    boolean isExistByNameAndEmail(NameAndEmail nameAndEmail);

    // 비밀번호 변경 (이름, 아이디 이용)
    int changePasswordByLoginId(NameAndLoginId nameAndLoginId, NewPassword newPassword);

    // 비밀번호 변경 (이름, 이메일 이용)
    int changePasswordByEmail(NameAndEmail nameAndEmail, NewPassword newPassword);

    // 비밀번호 변경 (현재 비밀번호 이용)
    int changePassword(String password, MypagePassword mypagePassword);

    // 로그인 한 유저 비밀번호와 비밀번호확인 입력 후 회원탈퇴버튼 누르면 해당 user 삭제
    int deleteUser(User user);

}
