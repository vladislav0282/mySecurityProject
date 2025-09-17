package com.example.mySecurityProject.dto;

import com.example.mySecurityProject.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String name;

    public UserDto() {

    }

    public static UserDto toUserDto(User entity){
        UserDto model = new UserDto();
        model.setId(entity.getId());
        model.setUsername(entity.getUsername());
        model.setName(entity.getName());
        return model;
    }
}
