package com.project.childprj.service;

import com.project.childprj.domain.mypage.NickName;
import com.project.childprj.domain.mypage.UserImage;
import com.project.childprj.domain.user.*;
import com.project.childprj.repository.AuthorityRepository;
import com.project.childprj.repository.UserRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Value("${app.upload.path}")
    private String uploadDir;

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
    public User findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean isExist(String loginId) {
        User user = findByLoginId(loginId);
        return (user != null);
    }

    @Override
    public boolean isExistById(Long id) {
        User user = findById(id);
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
    public int uploadProfile(MultipartFile file, Long userId) {
        int result = 0;
        // 물리적인 파일 저장 (파일이 없거나 이미지가 아니면 null 리턴)
        UserImage image = upload(file);

        // 성공한 경우 db 에도 저장
        if(image != null) {

            // 기존에 이미지가 있었다면 원래 있던 이미지 삭제
            if(userRepository.findUserImage(userId) != null){
                UserImage originUserImage = userRepository.findUserImage(userId);
                delFile(originUserImage);  // 물리적으로 파일 삭제
                userRepository.delImage(originUserImage);  // DB 에서 삭제
            }

            // 새로운 이미지 DB 에 저장
            image.setUserId(userId); // FK 설정
            result = userRepository.saveImage(image); // INSERT
        }

        return result;
    }

    @Override
    public UserImage findUserImage(Long userId) {
        return userRepository.findUserImage(userId);
    }

    // 물리적으로 파일 저장. 중복된 이름 rename 처리
    private UserImage upload(MultipartFile multipartFile) {
        UserImage userImage = null;

        // 담긴 파일이 없으면 pass~
        String originalFilename = multipartFile.getOriginalFilename();
        if(originalFilename == null || originalFilename.length() == 0) return null;

        // 파일이 이미지가 아니면 pass~
        String mimeType = multipartFile.getContentType();
        if(mimeType == null || !mimeType.contains("image")) return null;

        // 원본 파일 명
        String sourceName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        // 저장될 파일 명
        String fileName = sourceName;

        // 파일이 중복되는지 확인
        File file = new File(uploadDir, fileName);

        if(file.exists()){  // 이미 존재하는 파일명,  중복된다면 다른 이름은 변경하여 파일 저장
            // a.txt => a_2378142783946.txt  : time stamp 값을 활용할거다!
            // "a" => "a_2378142783946" : 확장자 없는 경우

            int pos = fileName.lastIndexOf(".");
            if(pos > -1){  // 확장자 있는 경우
                String name = fileName.substring(0, pos);   // 파일 '이름'
                String ext = fileName.substring(pos + 1);  // 파일 '확장자'

                fileName = name + "_" + System.currentTimeMillis() + "." + ext;
            } else {  // 확장자 없는 경우
                fileName += "_" + System.currentTimeMillis();
            }
        }

        Path copyOfLocation = Paths.get(new File(uploadDir, fileName).getAbsolutePath());

        try {
            // inputStream을 가져와서
            // copyOfLocation (저장위치)로 파일을 쓴다.
            // copy의 옵션은 기존에 존재하면 REPLACE(대체한다), 오버라이딩 한다
            Files.copy(
                    multipartFile.getInputStream(),
                    copyOfLocation,
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        userImage = UserImage.builder()
                .fileName(fileName)  // 저장된 이름
                .sourceName(sourceName)  // 원본 이름
                .build();

        return userImage;
    }

    private void delFile(UserImage image) {
        String saveDirectory = new File(uploadDir).getAbsolutePath();

        File f = new File(saveDirectory, image.getFileName());  // 물리적으로 저장된 파일들이 삭제 대상

        if(f.exists()){
            if(f.delete()){
                System.out.println("삭제 성공");
            } else {
                System.out.println("삭제 실패");
            }
        } else {
            System.out.println("파일이 존재하지 않습니다.");
        }

    }


    @Override
    public int updateNickName(NickName nickName) {
        return userRepository.updateNickName(nickName);
    }

    @Override
    public List<Authority> selectAuthoritiesById(Long id) {
        User user = userRepository.findById(id);
        return authorityRepository.findByUser(user);
    }

    @Override
    public String findLoginIdByNameAndEmail(NameAndEmail nameAndEmail) {
        User user = userRepository.findByNameAndEmail(nameAndEmail);
        String result;
        if (user != null) {
            result = user.getLoginId();
        } else {
            result = "";
        }
        return result;
    }

    @Override
    public boolean isExistByNameAndLoginId(NameAndLoginId nameAndLoginId) {
        User user = userRepository.findByNameAndLoginId(nameAndLoginId);
        return (user != null);
    }

    @Override
    public boolean isExistByNameAndEmail(NameAndEmail nameAndEmail) {
        User user = userRepository.findByNameAndEmail(nameAndEmail);
        return (user != null);
    }

    @Override
    public int changePasswordByLoginId(NameAndLoginId nameAndLoginId, NewPassword newPasswordInstance) {
        if(!isExistByNameAndLoginId(nameAndLoginId)) {
            return 0;
        }
        User user = userRepository.findByNameAndLoginId(nameAndLoginId);
        String newPassword = newPasswordInstance.getNewPassword();
        String re_password = newPasswordInstance.getRe_password();

        if(newPassword.equals(re_password)) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }
        return userRepository.updatePassword(user);
    }

    @Override
    public int changePasswordByEmail(NameAndEmail nameAndEmail, NewPassword newPasswordInstance) {
        if(!isExistByNameAndEmail(nameAndEmail)) {
            return 0;
        }
        User user = userRepository.findByNameAndEmail(nameAndEmail);
        String newPassword = newPasswordInstance.getNewPassword();
        String re_password = newPasswordInstance.getRe_password();

        if(newPassword.equals(re_password)) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }
        return userRepository.updatePassword(user);
    }

    @Override
    public int changePassword(MypagePassword mypagePassword) {
        User user = userRepository.findById(mypagePassword.getUserId());

        user.setPassword(passwordEncoder.encode(mypagePassword.getNewPassword()));

        return userRepository.updatePassword(user);
    }


    @Override
    public int deleteUser(User user) {
        if(!user.getPassword().equals(user.getRe_password())){
            return 0;
        }
        User originUser = userRepository.findById(user.getId());
        if(!passwordEncoder.matches(user.getPassword(), originUser.getPassword())) {
            return 0;
        }
        return userRepository.delete(user);
    }

}
