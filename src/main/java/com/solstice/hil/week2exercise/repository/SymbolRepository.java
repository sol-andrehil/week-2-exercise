package com.solstice.hil.week2exercise.repository;

import com.solstice.hil.week2exercise.model.Symbol;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SymbolRepository extends CrudRepository<Symbol, Long> {

}
