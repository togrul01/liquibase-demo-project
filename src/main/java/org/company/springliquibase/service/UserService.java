package org.company.springliquibase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.company.springliquibase.dao.UserRepository;
import org.company.springliquibase.exception.UserNotFoundException;
import org.company.springliquibase.mapper.UserMapper;
import org.company.springliquibase.model.UserRequest;
import org.company.springliquibase.model.UserResponse;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponse getUser(Long id) {
        log.info("ActionLog.getUser.start id: {}", id);
        var user =
                userRepository.findById(id).
                        orElseThrow(() -> {
                            log.error("ActionLog.getUser.error id: {}", id);
                            return new UserNotFoundException("USER_NOT_FOUND");
                        });
        log.info("ActionLog.getUser.end id: {}", id);
        return UserMapper.toUserResponse(user);
    }

    public void saveUser(UserRequest userRequest) {
        log.info("ActionLog.saveUser.start userRequest: {}", userRequest);

        if (userRepository.existsByUserName((userRequest.getUserName()))) {
            log.error("ActionLog.saveUser.error Username already exists: {}", userRequest.getUserName());
            throw new IllegalArgumentException("Username already exists: " + userRequest.getUserName());
        }

        userRepository.save(UserMapper.toUserEntity(userRequest));
        log.info("ActionLog.saveUser.end userRequest: {}", userRequest);
    }

}
