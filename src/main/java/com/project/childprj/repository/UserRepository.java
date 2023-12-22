package com.project.childprj.repository;

import com.project.childprj.domain.user.NickName;
import com.project.childprj.domain.user.NameAndEmail;
import com.project.childprj.domain.user.NameAndLoginId;
import com.project.childprj.domain.user.User;
import com.project.childprj.domain.user.UserImage;

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
    User findByNameAndEmail(NameAndEmail nameAndEmail);

    // 특정 name 과 loginId 의 user 리턴
    User findByNameAndLoginId(NameAndLoginId nameAndLoginId);

    // 해당 user 비밀번호 변경
    int updatePassword(User user);

    // 새로운 User 등록
    int save(User user);

    // userImage 리턴
    UserImage findUserImage(Long userId);

    // 새로운 userImage 추가
    int saveImage(UserImage image);

    // userImage 삭제
    int delImage(UserImage image);

    // User 정보 수정
    int updateNickName(NickName nickName);

    // 특정 user 삭제
    int delete(User user);

}
