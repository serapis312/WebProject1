package com.project.childprj.util;

import com.project.childprj.config.PrincipalDetails;
import com.project.childprj.domain.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class U {
    // 현재 request
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attrs.getRequest();
    }

    // 현재 session
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    // 현재 로그인 한 사용자 User 구하기
    public static User getLoggedUser(){
        // 현재 로그인 한 사용자
        PrincipalDetails userDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        return user;
    }

    // 첨부파일 이미지 여부 확인
    public static boolean isFileImage(MultipartFile file) {
        BufferedImage bufferedImage = null;

        try {
            bufferedImage = ImageIO.read(file.getInputStream());

            if(bufferedImage != null){
                System.out.printf("\t이미지 파일입니다: %d x %d\n", bufferedImage.getWidth(), bufferedImage.getHeight());
                return true;
            } else {
                System.out.println("\t이미지 파일이 아닙니다");
                return false;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
