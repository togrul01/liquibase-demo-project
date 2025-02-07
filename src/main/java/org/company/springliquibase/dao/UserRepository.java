package org.company.springliquibase.dao;

import org.company.springliquibase.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    boolean existsByUserName(String userName);
}
