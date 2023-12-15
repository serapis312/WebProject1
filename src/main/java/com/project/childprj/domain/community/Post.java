package com.project.childprj.domain.community;


import com.project.childprj.domain.user.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    private Long id;
    private String  title;
    private String content;
    private Long viewCnt;
    private LocalDateTime createDate;

    private User user;  // 글 작성자 (FK)

    // 첨부 파일
    @ToString.Exclude
    @Builder.Default
    private List<Attachment> fileList = new ArrayList<>();
}
