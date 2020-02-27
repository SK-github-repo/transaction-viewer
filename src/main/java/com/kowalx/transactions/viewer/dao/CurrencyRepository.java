package com.kowalx.transactions.viewer.dao;

import com.kowalx.transactions.viewer.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

    @Query("select c from Currency c where c.symbol = :givenSymbol")
    Optional<Currency> findBySymbol(@Param("givenSymbol") String symbol);
}
