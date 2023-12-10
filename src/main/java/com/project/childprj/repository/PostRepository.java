package com.project.childprj.repository;

import com.project.childprj.domain.Post;

import java.util.List;

public interface PostRepository {

    List<Post> findAll();
    int save(Post post);

    Post findById(Long id);

    int viewCnt(Long id);

    int update(Post post);
    int delete(Post post);
}
