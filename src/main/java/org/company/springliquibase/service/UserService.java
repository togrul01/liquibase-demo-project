package org.company.springliquibase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.company.springliquibase.dao.UserRepository;
import org.company.springliquibase.exception.UserNotFoundException;
import org.company.springliquibase.mapper.UserMapper;
import org.company.springliquibase.model.PageableUserResponse;
import org.company.springliquibase.model.UserRequest;
import org.company.springliquibase.model.UserResponse;
import org.company.springliquibase.model.criteria.PageCriteria;
import org.company.springliquibase.model.criteria.UserCriteria;
import org.company.springliquibase.specification.UserSpecification;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import static org.company.springliquibase.constants.CriteriaConstants.COUNT_DEFAULT_VALUE;
import static org.company.springliquibase.constants.CriteriaConstants.PAGE_DEFAULT_VALUE;
import static org.company.springliquibase.mapper.UserMapper.mapToPageableResponse;

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

    public PageableUserResponse getUsers(PageCriteria pageCriteria, UserCriteria userCriteria) {
        log.info("ActionLog.getUsers.start criteria: {}, userCriteria: {}", pageCriteria, userCriteria);
        var pageNumber = pageCriteria.getPage() == null ? PAGE_DEFAULT_VALUE : pageCriteria.getPage();
        var count = pageCriteria.getCount() == null ? COUNT_DEFAULT_VALUE : pageCriteria.getCount();
        var userPage = userRepository.findAll(new UserSpecification(userCriteria), PageRequest.of(pageNumber, count));
        log.info("ActionLog.getUsers.end criteria: {}, userCriteria: {}", pageCriteria, userCriteria);
        return mapToPageableResponse(userPage);
    }

}
