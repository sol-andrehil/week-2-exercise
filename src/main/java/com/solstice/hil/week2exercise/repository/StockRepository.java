package com.solstice.hil.week2exercise.repository;

import com.solstice.hil.week2exercise.model.Stock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface StockRepository extends CrudRepository<Stock, Long> {

    @Query("select max(s.price) from Stock s where s.date between :startDate and :endDate and symbol.name = :symbol")
    BigDecimal findMaxPriceByDate(@Param("symbol") String symbol, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("select min(price) from Stock where date between :startDate and :endDate and symbol.name = :symbol")
    BigDecimal findMinPriceByDate(@Param("symbol") String symbol, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("select price from Stock where date = (select max(date) from Stock where date between :startDate and :endDate) and symbol.name = :symbol")
    BigDecimal findClosingPriceByDate(@Param("symbol") String symbol, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("select sum(volume) from Stock where date between :startDate and :endDate and symbol.name = :symbol")
    Long sumVolumeByDate(@Param("symbol") String symbol, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
