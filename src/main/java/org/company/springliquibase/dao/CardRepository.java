package org.company.springliquibase.dao;


import org.company.springliquibase.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;


import java.util.List;
import java.util.Optional;

public interface CardRepository extends CrudRepository<CardEntity, Long>, JpaSpecificationExecutor<CardEntity> {
    @Override
    List<CardEntity> findAll();

    boolean existsByCardNumber(String cardNumber);

    Optional<CardEntity> findByCardNumber(String cardNumber);
}
