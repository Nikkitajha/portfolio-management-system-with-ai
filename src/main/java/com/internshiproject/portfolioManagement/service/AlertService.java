package com.internshiproject.portfolioManagement.service;

import com.internshiproject.portfolioManagement.entity.Stock;
import com.internshiproject.portfolioManagement.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AlertService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private StockRepository stockRepository;

    public void checkAlerts(Stock stock) {

        double entry = stock.getEntryPrice();
        double current = stock.getCurrentPrice();

        double percent = ((current - entry) / entry) * 100;
        double profitAmount = (current - entry) * stock.getQuantity();

        LocalDateTime now = LocalDateTime.now();

        // ✅ COOLDOWN (1 min)
        if (stock.getLastAlertDate() != null &&
                stock.getLastAlertDate().plusMinutes(1).isAfter(now)) {
            return;
        }

        // 🔴 STOP LOSS
        if (stock.getStopLoss() != null &&
                current <= stock.getStopLoss() &&
                !Boolean.TRUE.equals(stock.getStopLossTriggered())) {

            send(stock, "🛑 Stop Loss Hit", "Stop Loss Alert",
                    stock.getSymbol() + " hit stop-loss price!",
                    current, entry, percent, profitAmount, "#dc2626");

            stock.setStopLossTriggered(true);
            update(stock, percent);
            return;
        }

        // 🔻 LOSS ALERT
        if (percent <= -10) {

            send(stock, "🚨 Loss Alert", "Loss Alert",
                    stock.getSymbol() + " is down " + String.format("%.2f", percent) + "%",
                    current, entry, percent, profitAmount, "#ef4444");

            update(stock, percent);
            return;
        }

        // 🟢 PROFIT ALERT
        if (percent >= 20 || profitAmount >= 5000) {

            if (stock.getLastAlertPercent() == null ||
                    percent - stock.getLastAlertPercent() >= 5) {

                send(stock, "🚀 Profit Alert", "Profit Alert",
                        stock.getSymbol() + " profit: " + String.format("%.2f", percent) +
                                "% (₹" + String.format("%.2f", profitAmount) + ")",
                        current, entry, percent, profitAmount, "#16a34a");

                update(stock, percent);
                return;
            }
        }

        // 📊 TREND ALERT
        if (stock.getLastAlertPercent() != null &&
                percent - stock.getLastAlertPercent() >= 5) {

            send(stock, "📊 Trend Alert", "Trend Alert",
                    stock.getSymbol() + " moved up quickly!",
                    current, entry, percent, profitAmount, "#2563eb");

            update(stock, percent);
            return;
        }

        // ⚠️ TRAILING DROP
        if (stock.getHighestPercent() == null || percent > stock.getHighestPercent()) {
            stock.setHighestPercent(percent);
        } else if (stock.getHighestPercent() - percent >= 5) {

            send(stock, "⚠️ Profit Drop Alert", "Profit Drop Alert",
                    stock.getSymbol() + " dropped from peak profit!",
                    current, entry, percent, profitAmount, "#f59e0b");

            stock.setHighestPercent(percent);
            update(stock, percent);
            return;
        }

        // 🔁 RECOVERY
        if (stock.getLastAlertPercent() != null &&
                stock.getLastAlertPercent() < 0 &&
                percent > 0) {

            send(stock, "🔁 Recovery Alert", "Recovery Alert",
                    stock.getSymbol() + " recovered to profit!",
                    current, entry, percent, profitAmount, "#14b8a6");

            update(stock, percent);
        }

        stockRepository.save(stock);
    }

    // ✅ ONLY EMAIL CALL (CLEAN)
    private void send(Stock stock,
                      String subject,
                      String title,
                      String message,
                      double current,
                      double entry,
                      double percent,
                      double profitAmount,
                      String color) {

        String html = buildEmail(
                stock.getUser().getEmail(),
                stock.getSymbol(),
                title,
                message,
                current,
                entry,
                percent,
                profitAmount,
                color
        );

        emailService.sendHtmlMail(
                stock.getUser().getEmail(),
                subject,
                html
        );
    }

    // ✅ SIMPLE HTML DESIGN
    private String buildEmail(String user,
                              String symbol,
                              String title,
                              String message,
                              double current,
                              double entry,
                              double percent,
                              double profitAmount,
                              String color) {

        String percentColor = percent >= 0 ? "green" : "red";
        String amountColor = profitAmount >= 0 ? "green" : "red";

        return "<h2 style='color:" + color + "'>📈 " + title + "</h2>" +
                "<p>Hello " + user + ",</p>" +
                "<p><b>" + message + "</b></p>" +
                "<hr>" +
                "<p><b>Stock:</b> " + symbol + "</p>" +
                "<p><b>Entry:</b> ₹" + entry + "</p>" +
                "<p><b>Current:</b> ₹" + current + "</p>" +
                "<p><b>Change:</b> <span style='color:" + percentColor + "'>" +
                String.format("%.2f", percent) + "%</span></p>" +
                "<p><b>P/L:</b> <span style='color:" + amountColor + "'>₹" +
                String.format("%.2f", profitAmount) + "</span></p>" +
                "<hr><p style='font-size:12px;color:gray'>Auto alert from PMS</p>";
    }

    private void update(Stock stock, double percent) {
        stock.setLastAlertPercent(percent);
        stock.setLastAlertDate(LocalDateTime.now());
        stockRepository.save(stock);
    }
}