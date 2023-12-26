package com.project.childprj.domain.user;

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
    private Long user_id;
    private String sourceName;
    private String fileName;
}
