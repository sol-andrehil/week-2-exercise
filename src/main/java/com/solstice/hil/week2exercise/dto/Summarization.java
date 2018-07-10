package com.solstice.hil.week2exercise.dto;

import java.math.BigDecimal;

public class Summarization {

    private String description;
    private BigDecimal value;

    public Summarization() {
    }

    public Summarization(String description, BigDecimal value) {
        this.description = description;
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getValue() {
        return value;
    }
}
