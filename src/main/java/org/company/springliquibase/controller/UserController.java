package org.company.springliquibase.controller;

import lombok.RequiredArgsConstructor;
import org.company.springliquibase.model.response.PageableUserResponse;
import org.company.springliquibase.model.request.UserRequest;
import org.company.springliquibase.model.response.UserResponse;
import org.company.springliquibase.model.criteria.PageCriteria;
import org.company.springliquibase.model.criteria.UserCriteria;
import org.company.springliquibase.service.UserService;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController()
@RequestMapping("v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public void saveUser(@RequestBody UserRequest userRequest) {
        userService.saveUser(userRequest);
    }

    @GetMapping("/getUsers")
    public PageableUserResponse getUsers(PageCriteria pageCriteria, UserCriteria userCriteria) {
        return userService.getUsers(pageCriteria, userCriteria);
    }
}
