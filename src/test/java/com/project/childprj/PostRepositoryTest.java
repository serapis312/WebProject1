package com.project.childprj;

import com.project.childprj.domain.Post;
import com.project.childprj.repository.PostRepository;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostRepositoryTest {

    @Autowired
    private SqlSession sqlSession;

    @Test
    void test1(){
        PostRepository postRepository = sqlSession.getMapper(PostRepository.class);

        System.out.println("[최초]");
        postRepository.findAll().forEach(e -> System.out.println(e));

        Post post = Post.builder()
                .name("양효준")
                .title("자바자바")
                .content("스프링스프링")
                .build();

        System.out.println("[생성전] " + post);
        postRepository.save(post);  // DB 에서 생성된 id 값이 담겨 있다.
        System.out.println("[생성후] " + post);

        System.out.println("[신규 생성후]");
        postRepository.findAll().forEach(e -> System.out.println(e));

        Long id = post.getId();
        for(int i = 0; i < 5; i++){
            postRepository.viewCnt(id);
        }
        post = postRepository.findById(id);
        System.out.println("[조회수 증가후] " + post);

        post.setContent("감기 조심하세요");
        post.setTitle("덜덜덜덜");
        postRepository.update(post);
        post = postRepository.findById(id);
        System.out.println("[수정후] " + post);

        postRepository.delete(post);
        System.out.println("[삭제후]");
        postRepository.findAll().forEach(e -> System.out.println(e));


        System.out.println("테스트 완료");
    }
}
