package com.project.childprj.service;

import com.project.childprj.controller.BoardController;
import com.project.childprj.domain.Post;
import com.project.childprj.repository.PostRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService{
    private PostRepository postRepository;

    @Autowired
    public BoardServiceImpl(SqlSession sqlSession){
        postRepository = sqlSession.getMapper(PostRepository.class);
        System.out.println("BoardService() 생성");
    }

    @Override
    public List<Post> list() {
        return postRepository.findAll();
    }

    @Override
    public int write(Post post){
        return postRepository.save(post);
    }

    @Override
    public Post detail(Long id) {
        postRepository.viewCnt(id);
        Post post = postRepository.findById(id);
        return post;
    }

    @Override
    public Post selectById(Long id) {
        Post post = postRepository.findById(id);
        return post;
    }

    @Override
    public int update(Post post) {
        return postRepository.update(post);
    }

    @Override
    public int deleteById(Long id) {
        int result = 0;
        Post post = postRepository.findById(id);
        if(post != null){
            result = postRepository.delete(post);
        }
        return result;
    }
}
