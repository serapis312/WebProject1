package com.project.childprj.domain.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserImage {
    private Long id;
    private Long userId;
    private String sourceName;
    private String fileName;
}
