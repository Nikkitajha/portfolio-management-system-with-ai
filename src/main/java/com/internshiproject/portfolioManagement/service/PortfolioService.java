package com.internshiproject.portfolioManagement.service;

import com.internshiproject.portfolioManagement.dto.StockRequestDto;
import com.internshiproject.portfolioManagement.entity.*;
import com.internshiproject.portfolioManagement.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PortfolioService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private AlertService alertService;


    // ===================== ADD STOCK (BUY) =====================
    public void addStock(String email, StockRequestDto request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ALWAYS deduct money for every buy
        double totalAmount =
                request.getEntryPrice() * request.getQuantity();

        walletService.invest(
                user.getId(),
                totalAmount
        );


        // ===================== STOCK TABLE UPDATE =====================
        Stock existing =
                stockRepository.findByUserAndSymbol(
                        user,
                        request.getSymbol()
                );

        if (existing != null) {

            int newQty =
                    existing.getQuantity()
                            + request.getQuantity();

            double avgPrice =
                    (
                            (existing.getEntryPrice()
                                    * existing.getQuantity())
                                    +
                                    (request.getEntryPrice()
                                            * request.getQuantity())
                    )
                            / newQty;

            existing.setQuantity(newQty);
            existing.setEntryPrice(avgPrice);
            existing.setCap(request.getCap());

            stockRepository.save(existing);

        } else {

            Stock stock = new Stock();

            stock.setSymbol(request.getSymbol());
            stock.setQuantity(request.getQuantity());
            stock.setEntryPrice(request.getEntryPrice());
            stock.setCurrentPrice(request.getEntryPrice());
            stock.setEntryDate(LocalDate.now());
            stock.setCap(request.getCap());
            stock.setSector(request.getSector());
            stock.setUser(user);

            stockRepository.save(stock);

            alertService.checkAlerts(stock);
        }


        // ===================== PORTFOLIO TABLE UPDATE =====================
        Portfolio existingPortfolio =
                portfolioRepository.findByUserAndSymbol(
                        user,
                        request.getSymbol()
                );

        if (existingPortfolio != null) {

            int newQty =
                    existingPortfolio.getQuantity()
                            + request.getQuantity();

            double avgPrice =
                    (
                            (existingPortfolio.getEntryPrice()
                                    * existingPortfolio.getQuantity())
                                    +
                                    (request.getEntryPrice()
                                            * request.getQuantity())
                    )
                            / newQty;

            existingPortfolio.setQuantity(newQty);
            existingPortfolio.setEntryPrice(avgPrice);
            existingPortfolio.setCurrentPrice(
                    request.getEntryPrice()
            );

            portfolioRepository.save(
                    existingPortfolio
            );

        } else {

            Portfolio portfolio =
                    new Portfolio();

            portfolio.setSymbol(
                    request.getSymbol()
            );

            portfolio.setQuantity(
                    request.getQuantity()
            );

            portfolio.setEntryPrice(
                    request.getEntryPrice()
            );

            portfolio.setCurrentPrice(
                    request.getEntryPrice()
            );

            portfolio.setUser(user);

            portfolioRepository.save(
                    portfolio
            );
        }


        // ===================== SAVE BUY TRANSACTION =====================
        Transaction txn =
                new Transaction();

        txn.setUser(user);
        txn.setSymbol(request.getSymbol());
        txn.setQuantity(request.getQuantity());

        txn.setType("BUY");

        txn.setEntryPrice(
                request.getEntryPrice()
        );

        txn.setEntryDate(
                LocalDateTime.now()
        );

        txn.setSellPrice(0.0);
        txn.setProfitLoss(0.0);

        transactionRepository.save(txn);
    }


    // ===================== GET STOCKS =====================
    public List<Stock> getUserStocks(
            String email) {

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "User not found"
                                        )
                        );

        List<Stock> stocks =
                stockRepository.findByUser(user);

        for (Stock stock : stocks) {

            double entry =
                    stock.getEntryPrice();

            double current =
                    stock.getCurrentPrice();

            if (current == 0) {
                current = entry;
            }

            double newPrice =
                    current
                            * (
                            1
                                    +
                                    (
                                            Math.random()
                                                    * 0.1
                                                    - 0.05
                                    )
                    );

            stock.setCurrentPrice(
                    newPrice
            );

            stockRepository.save(stock);
        }

        return stocks;
    }


    // ===================== SELL STOCK =====================
    public void sellStock(
            String email,
            Long stockId,
            int sellQty) {

        // Prevent invalid quantity
        if (sellQty <= 0) {
            throw new RuntimeException(
                    "Quantity must be greater than zero"
            );
        }


        // Get logged-in user
        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "User not found"
                                        )
                        );


        // Find stock only if it belongs to logged-in user
        Stock stock =
                stockRepository.findByIdAndUser(
                                stockId,
                                user
                        )
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Stock not found or does not belong to user"
                                        )
                        );


        if (sellQty > stock.getQuantity()) {
            throw new RuntimeException(
                    "Not enough quantity"
            );
        }


        double sellPrice =
                stock.getCurrentPrice();

        stock.setCurrentPrice(
                sellPrice
        );


        double profit =
                (
                        sellPrice
                                - stock.getEntryPrice()
                )
                        * sellQty;


        // ===================== ADD MONEY BACK TO WALLET =====================
        double totalSellAmount =
                sellPrice * sellQty;

        walletService.addFunds(
                user.getId(),
                totalSellAmount
        );


        // ===================== SEND EMAIL =====================
        emailService.sendMail(
                user.getEmail(),
                "💰 Stock Sold Successfully",
                "Stock: "
                        + stock.getSymbol()
                        + "\nQuantity Sold: "
                        + sellQty
                        + "\nSell Price: ₹"
                        + sellPrice
                        + "\nBuy Price: ₹"
                        + stock.getEntryPrice()
                        + "\nProfit/Loss: ₹"
                        + profit
        );


        // ===================== ALERT =====================
        alertService.checkAlerts(
                stock
        );


        // ===================== SAVE SELL TRANSACTION =====================
        Transaction sellTxn =
                new Transaction();

        sellTxn.setUser(user);

        sellTxn.setSymbol(
                stock.getSymbol()
        );

        sellTxn.setQuantity(
                sellQty
        );

        sellTxn.setEntryPrice(
                stock.getEntryPrice()
        );

        sellTxn.setSellPrice(
                sellPrice
        );

        sellTxn.setEntryDate(
                LocalDateTime.now()
        );

        sellTxn.setSellDate(
                LocalDateTime.now()
        );

        sellTxn.setType("SELL");

        sellTxn.setProfitLoss(
                profit
        );

        transactionRepository.save(
                sellTxn
        );


        // ===================== UPDATE PORTFOLIO TABLE =====================
        Portfolio portfolio =
                portfolioRepository.findByUserAndSymbol(
                        user,
                        stock.getSymbol()
                );

        if (portfolio != null) {

            if (sellQty
                    > portfolio.getQuantity()) {

                throw new RuntimeException(
                        "Portfolio quantity mismatch"
                );
            }

            int remainingPortfolioQty =
                    portfolio.getQuantity()
                            - sellQty;

            if (remainingPortfolioQty == 0) {

                portfolioRepository.delete(
                        portfolio
                );

            } else {

                portfolio.setQuantity(
                        remainingPortfolioQty
                );

                portfolio.setCurrentPrice(
                        sellPrice
                );

                portfolioRepository.save(
                        portfolio
                );
            }
        }


        // ===================== UPDATE STOCK TABLE =====================
        int remainingQty =
                stock.getQuantity()
                        - sellQty;

        if (remainingQty == 0) {

            stockRepository.delete(
                    stock
            );

        } else {

            stock.setQuantity(
                    remainingQty
            );

            stockRepository.save(
                    stock
            );
        }
    }


    // ===================== TRANSACTION HISTORY =====================
    public List<Transaction> getHistory(
            String email) {

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "User not found"
                                        )
                        );

        return transactionRepository
                .findByUser(user);
    }


    // ===================== TOTAL PROFIT / LOSS =====================
    public double getTotalProfitLoss(
            String email) {

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "User not found"
                                        )
                        );

        List<Stock> stocks =
                stockRepository.findByUser(user);

        double total = 0;

        for (Stock stock : stocks) {

            total +=
                    (
                            stock.getCurrentPrice()
                                    - stock.getEntryPrice()
                    )
                            * stock.getQuantity();
        }

        return total;
    }


    // ===================== ADMIN =====================
    public List<Portfolio> getPortfolioByUserId(
            Long userId) {

        return portfolioRepository
                .findByUserId(userId);
    }
}
