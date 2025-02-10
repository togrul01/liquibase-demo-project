package org.company.springliquibase.mapper;

import org.company.springliquibase.entity.UserEntity;
import org.company.springliquibase.model.PageableUserResponse;
import org.company.springliquibase.model.UserRequest;
import org.company.springliquibase.model.UserResponse;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

public class UserMapper {
    public static UserResponse toUserResponse(UserEntity userEntity) {
        return UserResponse.builder()
                .id(userEntity.getId())
                .userName(userEntity.getUserName())
                .age(userEntity.getAge())
                .birthPlace(userEntity.getBirthPlace())
                .build();
    }

    public static UserEntity toUserEntity(UserRequest userRequest) {
        return UserEntity.builder()
                .userName(userRequest.getUserName())
                .age(userRequest.getAge())
                .birthPlace(userRequest.getBirthPlace())
                .build();
    }

    public static PageableUserResponse mapToPageableResponse(Page<UserEntity> userPage) {
        return PageableUserResponse.builder()
                .userResponses(userPage.getContent().stream().map(UserMapper::toUserResponse).
                        collect(Collectors.toList()))
                .lastPageNumber(userPage.getTotalPages())
                .totalElements(userPage.getTotalElements())
                .hasNextPage(userPage.hasNext())
                .build();
    }
}
