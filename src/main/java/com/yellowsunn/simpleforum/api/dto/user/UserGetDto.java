package com.yellowsunn.simpleforum.api.dto.user;

import com.yellowsunn.simpleforum.domain.user.Role;
import com.yellowsunn.simpleforum.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserGetDto {

    private Long id;
    private String username;
    private Role role;

    public UserGetDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
    }
}
