package com.project.childprj.repository;

import com.project.childprj.domain.community.Attachment;
import com.project.childprj.domain.community.Comment;
import com.project.childprj.domain.community.Post;
import com.project.childprj.domain.mypage.UserImage;

import java.util.List;

public interface CommunityRepository {
    // 새글 작성
    int save(Post post);

    // 특정 userId의 UserImage 찾기
    UserImage findUserImage(Long userId);

    // 특정 글에 특정 유저가 추천하기 추가
    int addRecommend(Long userId, Long postId);

    // 특정 게시물의 추천수 구하기
    int findRecommendCnt(Long postId);

    // 특정 id 글 내용 읽기 (SELECT)
    // 만약 해당 id 의 글 없으면 null 리턴함
    Post findById(Long id);

    // 특정 id 글 조회수 +1 증가 (UPDATE)
    int incViewCnt(Long id);

    // 전체 글 목록 : 최신순 (SELECT)
    List<Post> findAll();

    // 특정 id 글 수정 (제목, 내용) (UPDATE)
    int update(Post post);

    // 특정 id 글 삭제하기 (DELETE)
    int delete(Post post);



    // 페이징 pagination
    // from 부터 rows 개 만큼 select
    List<Post> selectFromRow(int from, int rows);

    // 전체 글의 개수
    int countAll();


    // 첨부파일
    // 첨부파일 DB 저장
    int saveImage(Attachment image);

    // 특정 글(postId) 의 첨부파일 목록
    List<Attachment> findImageByPost(Long postId);

    // 특정 첨부파일(id) 한개 select
    Attachment findImageById(Long id);

    // 특정 첨부 파일(file)을 DB에서 삭제
    int deleteImage(Attachment image);


    // 댓글
    // 특정 글(post_id) 의 댓글 목록
    List<Comment> findCommentByPost(Long postId);

    // 댓글 작성 <-- Comment
    int saveComment(Comment comment);

    // 특정 댓글 (id) 삭제
    int deleteCommentById(Long id);


}
