package com.yellowsunn.simpleboard.api.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yellowsunn.simpleboard.domain.user.Role;
import com.yellowsunn.simpleboard.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class UserGetDto {

    private Long id;
    private String username;
    private Role role;

    @JsonFormat(pattern = "yyyy년 MM월 dd일")
    private LocalDateTime createdDate;

    public UserGetDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
        this.createdDate = user.getCreatedDate();
    }
}
