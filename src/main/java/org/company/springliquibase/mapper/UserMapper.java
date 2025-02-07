package org.company.springliquibase.mapper;

import org.company.springliquibase.entity.UserEntity;
import org.company.springliquibase.model.UserRequest;
import org.company.springliquibase.model.UserResponse;

public class UserMapper {
    public static UserResponse toUserResponse(UserEntity userEntity) {
        return UserResponse.builder()
                .id(userEntity.getId())
                .userName(userEntity.getUserName())
                .age(userEntity.getAge())
                .build();
    }

    public static UserEntity toUserEntity(UserRequest userRequest) {
        return UserEntity.builder()
                .userName(userRequest.getUserName())
                .age(userRequest.getAge())
                .build();
    }
}
