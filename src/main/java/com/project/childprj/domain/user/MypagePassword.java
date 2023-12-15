package com.project.childprj.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MypagePassword {
    String nowPassword;
    String newPassword;
    String re_password;
    Long userId;
}
