package com.project.childprj.controller;

import com.project.childprj.domain.community.QryCommentList;
import com.project.childprj.domain.community.QryResult;
import com.project.childprj.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/community/comment")
public class CommunityCommentController {

    @Autowired
    private CommunityService communityService;

    @GetMapping("/list")
    public QryCommentList list(Long id){
        return communityService.listComment(id);
    }

    @PostMapping("/write")
    public QryResult write(
            @RequestParam("postId") Long postId,
            @RequestParam("userId") Long userId,
            String content){
        return communityService.writeComment(postId, userId, content);
    }

    @PostMapping("/delete")
    public QryResult delete(Long id){
        return communityService.deleteComment(id);
    }

}
