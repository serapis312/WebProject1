package com.project.childprj.service;

import com.project.childprj.domain.community.Post;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public class CommunityServiceImpl implements CommunityService {
    @Override
    public int write(Post post, Map<String, MultipartFile> files) {
        return 0;
    }

    @Override
    public int addRecommend(Long userId, Long postId) {
        return 0;
    }

    @Override
    public Post detail(Long id) {
        return null;
    }

    @Override
    public List<Post> list() {
        return null;
    }

    @Override
    public Post selectById(Long id) {
        return null;
    }

    @Override
    public int update(Post post, Map<String, MultipartFile> files, Long[] delfile) {
        return 0;
    }

    @Override
    public int deleteById(Long id) {
        return 0;
    }
}
