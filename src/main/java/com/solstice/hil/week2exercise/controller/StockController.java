package com.solstice.hil.week2exercise.controller;

import com.solstice.hil.week2exercise.dto.AggregationType;
import com.solstice.hil.week2exercise.dto.Summarization;
import com.solstice.hil.week2exercise.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @PostMapping(path = "/load")
    public void loadData() {
        stockService.loadDataFromFile();
    }

    @GetMapping(path = "/{symbol}/{date}")
    public Summarization getAggregationByDate(@PathVariable String symbol,
                                              @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                              @RequestParam AggregationType type) {
        return stockService.getAggregatedValueByDate(symbol, date, type);
    }

    @GetMapping(path = "/by_month/{symbol}/{yearMonth}")
    public Summarization getAggregationByMonth(@PathVariable String symbol,
                                               @PathVariable String yearMonth,
                                               @RequestParam AggregationType type) {
        String[] split = yearMonth.split("-");
        Integer year = new Integer(split[0]);
        Integer month = new Integer(split[1]);
        return stockService.getAggregatedValueByMonth(symbol, year, month, type);
    }

}
