package com.internshiproject.portfolioManagement.controller;

import com.internshiproject.portfolioManagement.dto.StockPrice;
import com.internshiproject.portfolioManagement.service.StockService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    // ─────────────────────────────────────────
    // GET /api/stock/price?symbol=RELIANCE
    // ─────────────────────────────────────────
    @GetMapping("/price")
    public ResponseEntity<?> getPrice(@RequestParam String symbol) {
        try {
            StockPrice price = stockService.getStockPrice(symbol);
            return ResponseEntity.ok(price);
        } catch (Exception e) {
            log.error("Invalid stock request: {}", symbol);
            return ResponseEntity
                    .badRequest()
                    .body("Stock not found. Please enter a valid symbol like TCS, RELIANCE.");

        }
    }

//invalid stock symbol
//    @GetMapping("/validate")
//    public ResponseEntity<?> validate(@RequestParam String symbol) {
//        try {
//            StockPrice stock = stockService.getStockPrice(symbol);
//            return ResponseEntity.ok(stock);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Invalid stock symbol");
//        }
//    }

    // ─────────────────────────────────────────
    // GET /api/stock/multiple?symbols=RELIANCE,TCS,INFY
    // ─────────────────────────────────────────
    @GetMapping("/multiple")
    public ResponseEntity<?> getMultiple(@RequestParam String symbols) {

        List<String> symbolList = Arrays.stream(symbols.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .toList();

        return ResponseEntity.ok(stockService.getMultipleStocks(symbolList));
    }

    // ─────────────────────────────────────────
    // GET /api/stock/nifty50
    // ─────────────────────────────────────────
    @GetMapping("/nifty50")
    public ResponseEntity<StockPrice> getNifty50() {
        return ResponseEntity.ok(stockService.getNifty50());
    }

    // ─────────────────────────────────────────
    // GET /api/stock/sensex
    // ─────────────────────────────────────────
    @GetMapping("/sensex")
    public ResponseEntity<StockPrice> getSensex() {
        return ResponseEntity.ok(stockService.getSensex());
    }

    // ─────────────────────────────────────────
    // GET /api/stock/banknifty
    // ─────────────────────────────────────────
    @GetMapping("/banknifty")
    public ResponseEntity<StockPrice> getBankNifty() {
        return ResponseEntity.ok(stockService.getBankNifty());
    }

    // ─────────────────────────────────────────
    // GET /api/stock/top — Top Indian Stocks
    // ─────────────────────────────────────────
    @GetMapping("/top")
    public ResponseEntity<List<StockPrice>> getTopStocks() {
        List<String> topStocks = Arrays.asList(
                "RELIANCE", "TCS", "HDFCBANK",
                "INFY",     "ICICIBANK", "HINDUNILVR",
                "SBIN",     "BAJFINANCE", "BHARTIARTL",
                "KOTAKBANK"
        );
        return ResponseEntity.ok(stockService.getMultipleStocks(topStocks));
    }
}