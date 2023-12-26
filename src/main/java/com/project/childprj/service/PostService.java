package com.project.childprj.service;

import com.project.childprj.domain.post.*;
import com.project.childprj.domain.user.UserImage;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface PostService {
    // 글 작성
    int write(Post post, Map<String, MultipartFile> files);

    // 특정 user_id 의 프로필 이미지 가져오기
    UserImage findUserImageByUserId(Long user_id);

    // 추천하기 추가
    int addRecommend(Long user_id, Long post_id);

    // 특정 id 의 글 조회
    // 트랜잭션 처리
    // 1. 조회수 증가 (UPDATE)
    // 2. 글 읽어오기 (SELECT)
    Post detail(Long id);

    // 글 목록
    List<Post> list();

    // 검색, 정렬순 글 목록
    List<Post> listPost(Integer page, String orderWay, String searchTxt, Model model);

    // 페이징 리스트
    List<Post> list(Integer page, Model model);


    // 특정 id 의 글 읽어오기 (SELECT)
    // 조회수 증가 없음
    Post selectById(Long id);

    // 특정 id 글 수정하기 (제목, 내용)  (UPDATE)
    int update(Post post, Map<String, MultipartFile> files, Long[] delfile);

    // 특정 id 의 글 삭제하기 (DELETE)
    int deleteById(Long id);


    // 첨부파일 관련 서비스
    // 특정 id 의 첨부이미지 가져오기
    Attachment findImageById(Long id);

    // 댓글 관련 서비스
    // 특정 글(id) 의 댓글 목록
    QryCommentList listComment(Long id);

    // 특정 글(post_id) 에 특정 사용자(user_id) 가 댓글 작성
    QryResult writeComment(Long post_id, Long user_id, String content);

    // 특정 댓글(id) 삭제
    QryResult deleteComment(Long id);

}
