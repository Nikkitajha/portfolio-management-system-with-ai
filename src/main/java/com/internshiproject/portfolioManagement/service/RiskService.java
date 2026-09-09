package com.internshiproject.portfolioManagement.service;

import com.internshiproject.portfolioManagement.entity.Stock;
import com.internshiproject.portfolioManagement.entity.User;
import com.internshiproject.portfolioManagement.repository.StockRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RiskService {

    @Autowired
    private StockRepository stockRepository;

    public String checkRisk(User user) {

        List<Stock> stocks = stockRepository.findByUser(user);

        double total = 0;

        for (Stock s : stocks) {
            total += s.getCurrentPrice() * s.getQuantity();
        }

        // Prevent division by zero
        if (total <= 0) {
            return "No portfolio data available";
        }

        // ===================== SINGLE STOCK CHECK =====================
        for (Stock s : stocks) {

            double value =
                    s.getCurrentPrice() * s.getQuantity();

            double percent =
                    (value / total) * 100;

            if (percent > 60) {
                return "⚠️ Too much invested in " + s.getSymbol();
            }
        }

        // ===================== SECTOR CHECK =====================
        Map<String, Double> sectorMap =
                new HashMap<>();

        for (Stock s : stocks) {

            double value =
                    s.getCurrentPrice() * s.getQuantity();

            String sector =
                    (s.getSector() == null ||
                            s.getSector().isEmpty())
                            ? "OTHER"
                            : s.getSector();

            sectorMap.put(
                    sector,
                    sectorMap.getOrDefault(
                            sector,
                            0.0
                    ) + value
            );
        }

        for (String sector : sectorMap.keySet()) {

            if ("OTHER".equals(sector)) {
                continue;
            }

            double percent =
                    (sectorMap.get(sector) / total) * 100;

            if (percent > 70) {
                return "⚠️ Over-invested in "
                        + sector
                        + " sector";
            }
        }

        return "SAFE";
    }
}
