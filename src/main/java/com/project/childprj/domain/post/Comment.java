package com.project.childprj.domain.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.project.childprj.domain.user.UserImage;
import com.project.childprj.domain.user.User;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    private Long id;  // PK

    @ToString.Exclude
    private UserImage userImage;  // 프로필 이미지

    @ToString.Exclude
    private User user;   // 댓글 작성자 (FK)
    @JsonIgnore
    private Long post_id;   // 어느글의 댓글 (FK)

    private String content;  // 댓글 내용

    // java.time.* 객체 포맷팅을 위한 annotation
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    @JsonProperty("createDate")
    private LocalDateTime createDate;  // 댓글 작성일시

}
