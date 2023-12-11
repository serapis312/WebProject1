package com.project.childprj.service;

import com.project.childprj.domain.Authority;
import com.project.childprj.domain.User;
import com.project.childprj.repository.AuthorityRepository;
import com.project.childprj.repository.UserRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;

    @Autowired
    public UserServiceImpl(SqlSession sqlSession){
        userRepository = sqlSession.getMapper(UserRepository.class);
        authorityRepository = sqlSession.getMapper(AuthorityRepository.class);
        System.out.println(getClass().getName() + "() 생성");
    }


    @Override
    public User findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId);
    }

    @Override
    public boolean isExist(String loginId) {
        User user = findByLoginId(loginId);
        return (user != null);
    }

    @Override
    public boolean isExistByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return (user != null);
    }

    @Override
    public boolean isExistByNickName(String nickName) {
        User user = userRepository.findByNickName(nickName);
        return (user != null);
    }

    @Override
    public int register(User user) {
        user.setLoginId(user.getLoginId().toUpperCase());   // DB 에는 회원아이디(username) 을 대문자로 저장
        user.setPassword(passwordEncoder.encode(user.getPassword()));   // password 는 PasswordEncoder 로 암호화 하여 저장해야 한다!
        int saveResult = userRepository.save(user);   // 새로이 회원 저장 (INSERT),  id 값 받아온다.

        // 신규 회원은 ROLE_MEMBER 권한 기본적으로 부여하기
        Authority auth = authorityRepository.findByAuthName("ROLE_MEMBER");

        Long userId = user.getId();
        Long authId = auth.getId();

        int authResult = authorityRepository.addAuthority(userId, authId);

        int result = 0;
        if(saveResult == 1 && authResult == 1) {
            result = 1;
        }

        return result;
    }

    @Override
    public List<Authority> selectAuthoritiesById(Long id) {
        User user = userRepository.findById(id);
        return authorityRepository.findByUser(user);
    }

    @Override
    public String findLoginIdByEmail(String email) {
        User user = userRepository.findByEmail(email);
        String loginId = user.getLoginId();
        return loginId;
    }


    // 비밀번호 찾기는 비밀번호가 암호화 되어있어서 안된다... 비밀번호 변경으로 하는건 어떨까??
    @Override
    public String findPasswordByLoginId(String loginId) {
        User user = userRepository.findByLoginId(loginId);
        String password =  user.getPassword();
        return null;
    }

    @Override
    public String findPasswordByEmail(String email) {
        return null;
    }
}
