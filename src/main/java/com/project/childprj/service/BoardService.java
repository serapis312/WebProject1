package com.project.childprj.service;

import com.project.childprj.domain.Post;

import java.util.List;

public interface BoardService {
    List<Post> list();

    int write(Post post);

    Post detail(Long id);
    Post selectById(Long id);

    int update(Post post);

    int deleteById(Long id);
}
