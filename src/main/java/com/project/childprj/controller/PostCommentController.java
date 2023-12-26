package com.project.childprj.controller;

import com.project.childprj.domain.post.QryCommentList;
import com.project.childprj.domain.post.QryResult;
import com.project.childprj.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board/comment")
public class PostCommentController {

    @Autowired
    private PostService postService;

    @GetMapping("/list")
    public QryCommentList list(@RequestParam("id") Long id){
        return postService.listComment(id);
    }

    @PostMapping("/write")
    public QryResult write(
            @RequestParam("postId") Long postId,
            @RequestParam("userId") Long userId,
            String content){
        return postService.writeComment(postId, userId, content);
    }

    @PostMapping("/delete")
    public QryResult delete(@RequestParam("id") Long id){
        return postService.deleteComment(id);
    }

}
