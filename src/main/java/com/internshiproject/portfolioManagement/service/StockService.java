package com.internshiproject.portfolioManagement.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.internshiproject.portfolioManagement.dto.StockPrice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class StockService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    //  NSE requires these headers to allow access
    private HttpClient buildClient() {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    private HttpRequest buildNseRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                //  Required NSE headers
                .header("User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "Chrome/120.0.0.0 Safari/537.36")
                .header("Accept", "application/json, text/plain, */*")
                .header("Accept-Language", "en-US,en;q=0.9")
                .header("Referer", "https://www.nseindia.com/")
                .header("Origin", "https://www.nseindia.com")
                .GET()
                .build();
    }

    // ─────────────────────────────────────────
    // Get Single Stock Price by Symbol
    // Example: RELIANCE, TCS, INFY, HDFCBANK
    // ─────────────────────────────────────────

    public StockPrice getStockPrice(String symbol) {
        try {

            symbol = symbol.toUpperCase().trim();

            HttpClient client = buildClient();

            //  FIX: handle index symbols like ^NSEI, ^BSESN
            String formattedSymbol;
            if (symbol.startsWith("^")) {
                formattedSymbol = symbol; // no .NS
            } else {
                formattedSymbol = symbol + ".NS";
            }

            // Encode symbol (THIS IS THE FIX)
            String encodedSymbol = URLEncoder.encode(formattedSymbol, StandardCharsets.UTF_8);

            //  FIX: build correct URL
            String url = "https://query1.finance.yahoo.com/v8/finance/chart/"
                    + encodedSymbol + "?interval=1d&range=1d";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(15))
                    .header("User-Agent", "Mozilla/5.0")
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode root = objectMapper.readTree(response.body());

            JsonNode result = root.path("chart").path("result");

            if (!result.isArray() || result.size() == 0) {
                throw new RuntimeException("Invalid stock symbol: " + symbol);
            }

            JsonNode meta = result.get(0).path("meta");

            StockPrice stock = new StockPrice();
            stock.setSymbol(symbol);
            stock.setCompanyName(getCompanyName(symbol)); // your helper
            stock.setLastPrice(meta.path("regularMarketPrice").asDouble());
            stock.setPreviousClose(meta.path("previousClose").asDouble());
            stock.setOpen(meta.path("regularMarketOpen").asDouble());
            stock.setHigh(meta.path("regularMarketDayHigh").asDouble());
            stock.setLow(meta.path("regularMarketDayLow").asDouble());
            stock.setTotalTradedVolume(meta.path("regularMarketVolume").asLong());
            stock.setLastUpdateTime(meta.path("regularMarketTime").asText());

            double change = stock.getLastPrice() - stock.getPreviousClose();
            double pChange = (change / stock.getPreviousClose()) * 100;

            stock.setChange(Math.round(change * 100.0) / 100.0);
            stock.setPChange(Math.round(pChange * 100.0) / 100.0);

            log.info("[Stock] {} = ₹{}", symbol, stock.getLastPrice());

            return stock;

        } catch (Exception e) {
            log.error("[Stock] Error fetching {}: {}", symbol, e.getMessage());
            return new StockPrice(); // or null
        }
    }


    // ─────────────────────────────────────────
    // Get Multiple Stocks at Once
    // ─────────────────────────────────────────
    public List<StockPrice> getMultipleStocks(List<String> symbols) {
        return symbols.parallelStream()
                .map(symbol -> {
                    try {
                        return getStockPrice(symbol);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    // ─────────────────────────────────────────
    // Get NIFTY 50 Index
    // ─────────────────────────────────────────
    public StockPrice getNifty50() {
        return getStockPrice("^NSEI");
    }

    // ─────────────────────────────────────────
    // Get SENSEX Index
    // ─────────────────────────────────────────
    public StockPrice getSensex() {
        return getStockPrice("^BSESN");
    }

    // ─────────────────────────────────────────
    // Get BANK NIFTY Index
    // ─────────────────────────────────────────
    public StockPrice getBankNifty() {
        return getStockPrice("^NSEBANK");
    }

    // ─────────────────────────────────────────
// Helper Method: Get Company Name
// ─────────────────────────────────────────
    private String getCompanyName(String symbol) {
        return switch (symbol) {
            case "TCS" -> "Tata Consultancy Services";
            case "RELIANCE" -> "Reliance Industries";
            case "INFY" -> "Infosys";
            case "HDFCBANK" -> "HDFC Bank";
            case "ICICIBANK" -> "ICICI Bank";
            case "SBIN" -> "State Bank of India";
            case "LT" -> "Larsen & Toubro";
            case "BHARTIARTL" -> "Bharti Airtel";
            case "KOTAKBANK" -> "Kotak Mahindra Bank";
            default -> symbol; // fallback
        };
    }
}