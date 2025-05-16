package org.company.springliquibase.service;

import org.company.springliquibase.model.criteria.UserCriteria;
import org.company.springliquibase.model.request.UserRequest;
import org.company.springliquibase.model.response.PageableUserResponse;
import org.company.springliquibase.model.response.Response;
import org.company.springliquibase.model.response.UserResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<Response<UserResponse>> createUser(UserRequest request);
    ResponseEntity<Response<UserResponse>> getUser(Long userId);
    ResponseEntity<Response<UserResponse>> getUser(String userName);
    ResponseEntity<Response<PageableUserResponse>> getUsers(UserCriteria criteria, int page, int size);
    ResponseEntity<Response<Void>> deleteUser(Long userId);
    ResponseEntity<Response<UserResponse>> updateUser(Long userId, UserRequest request);
    ResponseEntity<Response<PageableUserResponse>> findAll(String name, int page, int size);
}
