package com.solstice.hil.week2exercise.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.solstice.hil.week2exercise.model.Symbol;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class StockDto {

    private String symbol;
    private BigDecimal price;
    private Integer volume;
    @JsonFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS+SSSS")
    private LocalDateTime date;


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
