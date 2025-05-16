package org.company.springliquibase.mapper;

import org.company.springliquibase.entity.UserEntity;
import org.company.springliquibase.model.request.UserRequest;
import org.company.springliquibase.model.response.PageableUserResponse;
import org.company.springliquibase.model.response.UserResponse;
import org.springframework.data.domain.Page;

public final class UserMapper {

    private UserMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static UserEntity toEntity(UserRequest request) {
        return UserEntity.builder()
                .userName(request.getUserName())
                .age(request.getAge())
                .birthPlace(request.getBirthPlace())
                .build();
    }

    public static UserResponse toResponse(UserEntity entity) {
        return UserResponse.builder()
                .id(entity.getId())
                .userName(entity.getUserName())
                .age(entity.getAge())
                .birthPlace(entity.getBirthPlace())
                .build();
    }

    public static void updateEntity(UserEntity entity, UserRequest request) {
        entity.setUserName(request.getUserName());
        entity.setAge(request.getAge());
        entity.setBirthPlace(request.getBirthPlace());
    }

    public static PageableUserResponse mapToPageableResponse(Page<UserEntity> userPage) {
        return PageableUserResponse.builder()
                .userList(userPage.getContent().stream()
                        .map(UserMapper::toResponse)
                        .toList())
                .pageNumber(userPage.getNumber())
                .pageSize(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .lastPageNumber(userPage.getTotalPages())
                .hasNextPage(userPage.hasNext())
                .build();
    }
}
