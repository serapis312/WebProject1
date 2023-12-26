package com.project.childprj.config;

import com.project.childprj.domain.user.User;
import com.project.childprj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PrincipalDetailService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername(" + username + ") 호출");

        // DB 조회
        User user = userService.findByUsername(username);

        // 해당 username 의 user 가 DB 에 있다면
        // UserDetails 생성해서 리턴
        if(user != null){
            PrincipalDetails userDetails = new PrincipalDetails(user);
            userDetails.setUserService(userService);
            return userDetails;
        }

        // 해당 username 의 user 가 DB 에 없다면?
        throw new UsernameNotFoundException(username);
    }
}
