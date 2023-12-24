package com.project.childprj.config;

import com.project.childprj.domain.user.Authority;
import com.project.childprj.domain.user.User;
import com.project.childprj.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PrincipalDetails implements UserDetails {

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("getAuthorities() 호출");

        Collection<GrantedAuthority> collect = new ArrayList<>();

        List<Authority> list = userService.selectAuthoritiesById(user.getId());    // DB 에서 user 의 권한(들)을 읽어오기

        for(Authority auth : list){
            collect.add(new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return auth.getAuthName();
                }

                // thymeleaf 등에서 활용할 목적 (학습목적)
                @Override
                public String toString() {
                    return auth.getAuthName();
                }
            });
        }

        return collect;
    }

    // 로그인한 사용자 정보
    private User user;

    public User getUser() {
        return user;
    }

    public PrincipalDetails(User user){
        System.out.println("UserDetails(user) 생성: " + user);
        this.user = user;
    }


    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLoginId();
    }

    // 계정이 만료되진 않았는지?
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠긴건 아닌지?
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정 credential 이 만료된건 아닌지
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정이 활성화 되었는지?
    @Override
    public boolean isEnabled() {
        return true;
    }
}
