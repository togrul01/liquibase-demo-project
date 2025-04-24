package org.company.springliquibase.dao;


import org.company.springliquibase.entity.CardEntity;
import org.company.springliquibase.enums.CardStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


import java.util.List;
import java.util.Optional;

public interface CardRepository extends CrudRepository<CardEntity, Long>, JpaSpecificationExecutor<CardEntity> {
    List<CardEntity> findAllByStatusNot(CardStatus status);
    @Override
    List<CardEntity> findAll();

    boolean existsByCardNumber(String cardNumber);

    Optional<CardEntity> findByCardNumber(String cardNumber);

    @Modifying // batch processing
    @Query("UPDATE CardEntity c SET c.balance = c.balance * 1.05 WHERE c.status = 'ACTIVE'")
    void increaseActiveCardBalances();

}
