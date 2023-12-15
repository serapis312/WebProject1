package com.project.childprj.service;

import com.project.childprj.domain.community.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CommunityService {
    // 글 작성
    int write(Post post, Map<String, MultipartFile> files);

    // 추천하기 추가
    int addRecommend(Long userId, Long postId);

    // 특정 id 의 글 조회
    // 트랜잭션 처리
    // 1. 조회수 증가 (UPDATE)
    // 2. 글 읽어오기 (SELECT)
    Post detail(Long id);

    // 글 목록
    List<Post> list();

    // 페이징 리스트



    // 특정 id 의 글 읽어오기 (SELECT)
    // 조회수 증가 없음
    Post selectById(Long id);

    // 특정 id 글 수정하기 (제목, 내용)  (UPDATE)
    int update(Post post, Map<String, MultipartFile> files, Long[] delfile);

    // 특정 id 의 글 삭제하기 (DELETE)
    int deleteById(Long id);

}
