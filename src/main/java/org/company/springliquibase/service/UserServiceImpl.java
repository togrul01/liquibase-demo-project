package org.company.springliquibase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.company.springliquibase.constants.LogConstants;
import org.company.springliquibase.entity.UserEntity;
import org.company.springliquibase.enums.Result;
import org.company.springliquibase.exception.UserNotFoundException;
import org.company.springliquibase.mapper.UserMapper;
import org.company.springliquibase.model.criteria.UserCriteria;
import org.company.springliquibase.model.request.UserRequest;
import org.company.springliquibase.model.response.PageableUserResponse;
import org.company.springliquibase.model.response.Response;
import org.company.springliquibase.model.response.UserResponse;
import org.company.springliquibase.repository.UserRepository;
import org.company.springliquibase.repository.specification.UserSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.company.springliquibase.util.LocalizationUtil.getLocalizedMessageByKey;
import static org.company.springliquibase.constants.LogConstants.Error.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private static final String ERROR_BUNDLE = "i18n/error";
    private static final String SUCCESS_BUNDLE = "i18n/success";
    private static final String USER_NOT_FOUND_KEY = "user.not.found";

    @Override
    @Transactional
    public ResponseEntity<Response<UserResponse>> createUser(UserRequest request) {
        log.info("Creating user with request: {}", request);
        try {
            var user = UserMapper.toEntity(request);
            var savedUser = userRepository.save(user);
            var response = UserMapper.toResponse(savedUser);
            var successMessage = getLocalizedMessageByKey(SUCCESS_BUNDLE, "user.create.success");
            log.info(successMessage);
            return ResponseEntity.ok(new Response<>(Result.USER_CREATED.getCode(), successMessage, response));
        } catch (Exception e) {
            var errorMessage = getLocalizedMessageByKey(ERROR_BUNDLE, "user.create.error");
            log.error(ERROR_CREATING_USER, e.getMessage());
            return ResponseEntity.badRequest().body(new Response<>(Result.ERROR.getCode(), errorMessage,
                    null));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Response<UserResponse>> getUser(Long userId) {
        log.info("Getting user with id: {}", userId);
        try {
            var user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
            var response = UserMapper.toResponse(user);
            var successMessage = getLocalizedMessageByKey(SUCCESS_BUNDLE, "user.get.success");
            log.info(successMessage);
            return ResponseEntity.ok(new Response<>(Result.USER_FOUND.getCode(), successMessage, response));
        } catch (UserNotFoundException e) {
            var errorMessage = getLocalizedMessageByKey(ERROR_BUNDLE, USER_NOT_FOUND_KEY);
            log.error(LogConstants.Error.USER_NOT_FOUND_WITH_ID, userId);
            return ResponseEntity.status(404).body(new Response<>(Result.USER_NOT_FOUND.getCode(),
                    errorMessage, null));
        } catch (Exception e) {
            var errorMessage = getLocalizedMessageByKey(ERROR_BUNDLE, "user.get.error");
            log.error(LogConstants.Error.ERROR_GETTING_USER, e.getMessage());
            return ResponseEntity.badRequest().body(new Response<>(Result.ERROR.getCode(), errorMessage,
                    null));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Response<UserResponse>> getUser(String userName) {
        log.info("Getting user with username: {}", userName);
        try {
            var user = userRepository.findByUserName(userName)
                    .orElseThrow(() -> new UserNotFoundException("User not found with username: " + userName));
            var response = UserMapper.toResponse(user);
            var successMessage = getLocalizedMessageByKey(SUCCESS_BUNDLE, "user.get.success");
            log.info(successMessage);
            return ResponseEntity.ok(new Response<>(Result.USER_FOUND.getCode(), successMessage, response));
        } catch (UserNotFoundException e) {
            var errorMessage = getLocalizedMessageByKey(ERROR_BUNDLE, USER_NOT_FOUND_KEY);
            log.error(LogConstants.Error.USER_NOT_FOUND_WITH_ID, userName);
            return ResponseEntity.status(404).body(new Response<>(Result.USER_NOT_FOUND.getCode(),
                    errorMessage, null));
        } catch (Exception e) {
            var errorMessage = getLocalizedMessageByKey(ERROR_BUNDLE, "user.get.error");
            log.error(ERROR_GETTING_USER, e.getMessage());
            return ResponseEntity.badRequest().body(new Response<>(Result.ERROR.getCode(),
                    errorMessage, null));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Response<PageableUserResponse>> getUsers(UserCriteria criteria, int page, int size) {
        log.info("Getting users with criteria: {}, page: {}, size: {}", criteria, page, size);
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
            Specification<UserEntity> spec = new UserSpecification(criteria);
            Page<UserEntity> userPage = userRepository.findAll(spec, pageable);
            var response = UserMapper.mapToPageableResponse(userPage);
            var successMessage = getLocalizedMessageByKey(SUCCESS_BUNDLE, "user.list.success");
            log.info(successMessage);
            return ResponseEntity.ok(new Response<>(Result.USER_FOUND.getCode(), successMessage, response));
        } catch (Exception e) {
            var errorMessage = getLocalizedMessageByKey(ERROR_BUNDLE, "user.list.error");
            log.error(LogConstants.Error.ERROR_FINDING_USERS, e.getMessage());
            return ResponseEntity.badRequest().body(new Response<>(Result.ERROR.getCode(),
                    errorMessage, null));
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Response<Void>> deleteUser(Long userId) {
        log.info("Deleting user with id: {}", userId);
        try {
            if (!userRepository.existsById(userId)) {
                throw new UserNotFoundException("User not found with id: " + userId);
            }
            userRepository.deleteById(userId);
            var successMessage = getLocalizedMessageByKey(SUCCESS_BUNDLE, "user.delete.success");
            log.info(successMessage);
            return ResponseEntity.ok(new Response<>(Result.USER_DELETED.getCode(), successMessage, null));
        } catch (UserNotFoundException e) {
            var errorMessage = getLocalizedMessageByKey(ERROR_BUNDLE, USER_NOT_FOUND_KEY);
            log.error(LogConstants.Error.USER_NOT_FOUND_WITH_ID, userId);
            return ResponseEntity.status(404).body(new Response<>(Result.USER_NOT_FOUND.getCode(),
                    errorMessage, null));
        } catch (Exception e) {
            var errorMessage = getLocalizedMessageByKey(ERROR_BUNDLE, "user.delete.error");
            log.error(ERROR_DELETING_USER, e.getMessage());
            return ResponseEntity.badRequest().body(new Response<>(Result.ERROR.getCode(), errorMessage, null));
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Response<UserResponse>> updateUser(Long userId, UserRequest request) {
        log.info("Updating user with id: {} and request: {}", userId, request);
        try {
            var user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
            UserMapper.updateEntity(user, request);
            var updatedUser = userRepository.save(user);
            var response = UserMapper.toResponse(updatedUser);
            var successMessage = getLocalizedMessageByKey(SUCCESS_BUNDLE, "user.update.success");
            log.info(successMessage);
            return ResponseEntity.ok(new Response<>(Result.USER_UPDATED.getCode(), successMessage, response));
        } catch (UserNotFoundException e) {
            var errorMessage = getLocalizedMessageByKey(ERROR_BUNDLE, USER_NOT_FOUND_KEY);
            log.error(USER_NOT_FOUND_WITH_ID, userId);
            return ResponseEntity.status(404).body(new Response<>(Result.USER_NOT_FOUND.getCode(),
                    errorMessage, null));
        } catch (Exception e) {
            var errorMessage = getLocalizedMessageByKey(ERROR_BUNDLE, "user.update.error");
            log.error(ERROR_UPDATING_USER, e.getMessage());
            return ResponseEntity.badRequest().body(new Response<>(Result.ERROR.getCode(),
                    errorMessage, null));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Response<PageableUserResponse>> findAll(String name, int page, int size) {
        log.info("Finding users with name: {}, page: {}, size: {}", name, page, size);
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
            Page<UserEntity> userPage = userRepository.findByUserNameContainingIgnoreCase(name, pageable);
            var response = UserMapper.mapToPageableResponse(userPage);
            var successMessage = getLocalizedMessageByKey(SUCCESS_BUNDLE, "user.find.success");
            log.info(successMessage);
            return ResponseEntity.ok(new Response<>(Result.USER_FOUND.getCode(), successMessage, response));
        } catch (Exception e) {
            var errorMessage = getLocalizedMessageByKey(ERROR_BUNDLE, "user.find.error");
            log.error("Error finding users: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new Response<>(Result.ERROR.getCode(), errorMessage, null));
        }
    }
} 