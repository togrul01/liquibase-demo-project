package org.company.springliquibase.controller;

import lombok.RequiredArgsConstructor;
import org.company.springliquibase.model.response.PageableUserResponse;
import org.company.springliquibase.model.request.UserRequest;
import org.company.springliquibase.model.response.Response;
import org.company.springliquibase.model.response.UserResponse;
import org.company.springliquibase.model.criteria.PageCriteria;
import org.company.springliquibase.model.criteria.UserCriteria;
import org.company.springliquibase.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController()
@RequestMapping("v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<Response<UserResponse>> getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseEntity<Response<UserResponse>> saveUser(@RequestBody UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @GetMapping("/getUsers")
    public ResponseEntity<Response<PageableUserResponse>> getUsers(PageCriteria pageCriteria, UserCriteria userCriteria) {
        return userService.getUsers(userCriteria, pageCriteria.getPage(), pageCriteria.getSize());
    }
}
