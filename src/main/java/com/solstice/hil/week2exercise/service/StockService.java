package com.solstice.hil.week2exercise.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solstice.hil.week2exercise.dto.AggregationType;
import com.solstice.hil.week2exercise.dto.StockDto;
import com.solstice.hil.week2exercise.dto.Summarization;
import com.solstice.hil.week2exercise.model.Stock;
import com.solstice.hil.week2exercise.model.Symbol;
import com.solstice.hil.week2exercise.repository.StockRepository;
import com.solstice.hil.week2exercise.repository.SymbolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Service
public class StockService {

    @Value("${endpoint.load.data.url}")
    private String loadDataUrl;

    @Autowired
    private SymbolRepository symbolRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private ObjectMapper objectMapper;

    public void loadDataFromFile() {
        try (InputStream inputStream = new URL(loadDataUrl).openStream()) {
            List<StockDto> stockDtoList = objectMapper.readValue(inputStream, new TypeReference<List<StockDto>>(){});
            List<Stock> stockList = new ArrayList<>();
            Map<String, Symbol> symbols = new HashMap<>();
            for (StockDto dto : stockDtoList) {
                Symbol symbol = symbols.get(dto.getSymbol());
                if (symbol == null) {
                    symbol = new Symbol();
                    symbol.setName(dto.getSymbol());
                    symbol = symbolRepository.save(symbol);
                    symbols.put(dto.getSymbol(), symbol);
                }
                Stock stock = new Stock();
                stock.setDate(dto.getDate());
                stock.setPrice(dto.getPrice());
                stock.setVolume(dto.getVolume());
                stock.setSymbol(symbol);
                stockList.add(stock);
            }
            stockRepository.saveAll(stockList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Summarization getAggregatedValueByDate(String symbol, LocalDate date, AggregationType type) {
        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = date.atTime(LocalTime.MAX);
        return getSummarization(symbol, startDate, endDate, type);
    }

    public Summarization getAggregatedValueByMonth(String symbol, Integer year, Integer month, AggregationType type) {
        LocalDateTime startDate = LocalDate.of(year, month, 1).atStartOfDay();
        LocalDateTime endDate = startDate.with(lastDayOfMonth()).with(LocalTime.MAX);
        return getSummarization(symbol, startDate, endDate, type);
    }

    private Summarization getSummarization(String symbol, LocalDateTime startDate, LocalDateTime endDate, AggregationType type) {
        BigDecimal value = null;
        String description;
        switch (type) {
            case MIN_PRICE:
                value = stockRepository.findMinPriceByDate(symbol, startDate, endDate);
                description = "Lowest price";
                break;
            case CLOSING_PRICE:
                value = stockRepository.findClosingPriceByDate(symbol, startDate, endDate);
                description = "Closing price";
                break;
            case TOTAL_VOLUME:
                Long longValue = stockRepository.sumVolumeByDate(symbol, startDate, endDate);
                if (longValue != null) {
                    value = new BigDecimal(longValue);
                }
                description = "Total volume";
                break;
            default:
                value = stockRepository.findMaxPriceByDate(symbol, startDate, endDate);
                description = "Highest price";
        }
        return new Summarization(description, value);

    }

}
