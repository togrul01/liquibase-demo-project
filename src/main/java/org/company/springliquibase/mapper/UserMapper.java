package org.company.springliquibase.mapper;

import org.company.springliquibase.entity.UserEntity;
import org.company.springliquibase.model.response.PageableUserResponse;
import org.company.springliquibase.model.request.UserRequest;
import org.company.springliquibase.model.response.UserResponse;
import org.springframework.data.domain.Page;

public final class UserMapper {

    private UserMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

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
                .userResponses(userPage.getContent().stream()
                        .map(UserMapper::toUserResponse)
                        .toList())
                .lastPageNumber(userPage.getTotalPages())
                .totalElements(userPage.getTotalElements())
                .hasNextPage(userPage.hasNext())
                .build();
    }

}
