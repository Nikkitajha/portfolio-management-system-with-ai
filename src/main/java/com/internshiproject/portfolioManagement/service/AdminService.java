package com.internshiproject.portfolioManagement.service;
import com.internshiproject.portfolioManagement.dto.AdminDashboardDto;
import com.internshiproject.portfolioManagement.dto.PortfolioDto;
import com.internshiproject.portfolioManagement.entity.Portfolio;
import com.internshiproject.portfolioManagement.entity.Transaction;
import com.internshiproject.portfolioManagement.entity.User;
import com.internshiproject.portfolioManagement.repository.StockRepository;
import com.internshiproject.portfolioManagement.repository.TransactionRepository;
import com.internshiproject.portfolioManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Service
    public class AdminService {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PortfolioService portfolioService;

        @Autowired
        private TransactionRepository transactionRepository;
    @Autowired
    private StockRepository stockRepository;

    //  GET ALL USERS
        public List<User> getAllUsers() {
            return userRepository.findAll();
        }


        // GET DASHBOARD DATA FOR A USER
        public AdminDashboardDto getDashboard(Long userId) {

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<Portfolio> portfolio = portfolioService.getPortfolioByUserId(userId);

            double totalInvestment = 0;
            double totalCurrentValue = 0;

            for (Portfolio p : portfolio) {
                totalInvestment += p.getEntryPrice() * p.getQuantity();
                totalCurrentValue += p.getCurrentPrice() * p.getQuantity();
            }

            double profitLoss = totalCurrentValue - totalInvestment;

            AdminDashboardDto dto = new AdminDashboardDto();
            dto.setUserId(user.getId());
            dto.setEmail(user.getEmail());
            dto.setTotalInvestment(totalInvestment);
            dto.setTotalCurrentValue(totalCurrentValue);
            dto.setProfitLoss(profitLoss);

            return dto;
        }

        //  GET USER PORTFOLIO (DTO SAFE)
        public List<PortfolioDto> getUserPortfolio(Long userId) {

            List<Portfolio> portfolioList = portfolioService.getPortfolioByUserId(userId);

            return portfolioList.stream()
                    .map(p -> new PortfolioDto(
                            p.getSymbol(),
                            p.getQuantity(),
                            p.getEntryPrice(),
                            p.getCurrentPrice()
                    ))
                    .toList();
        }

//getUser history
public List<Map<String, Object>> getUserHistory(Long userId) {

    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    List<Transaction> transactions = transactionRepository.findByUser(user);

    return transactions.stream().map(tx -> {
        Map<String, Object> map = new HashMap<>();

        map.put("symbol", tx.getSymbol());
        map.put("quantity", tx.getQuantity());
        map.put("type", tx.getType());

        map.put("entryPrice", tx.getEntryPrice());
        map.put("sellPrice", tx.getSellPrice());
        map.put("profitLoss", tx.getProfitLoss());

        map.put("entryDate", tx.getEntryDate());
        map.put("sellDate", tx.getSellDate());

        return map;
    }).toList();
}

    public List<AdminDashboardDto> getAllUsersWithStats() {

        List<User> users = userRepository.findAll();

        return users.stream().map(user -> {

            List<Portfolio> portfolio = portfolioService.getPortfolioByUserId(user.getId());

            double totalInvestment = 0;
            double totalCurrentValue = 0;

            for (Portfolio p : portfolio) {
                totalInvestment += p.getEntryPrice() * p.getQuantity();
                totalCurrentValue += p.getCurrentPrice() * p.getQuantity();
            }

            double profitLoss = totalCurrentValue - totalInvestment;

            AdminDashboardDto dto = new AdminDashboardDto();
            dto.setUserId(user.getId());
            dto.setEmail(user.getEmail());
            dto.setTotalInvestment(totalInvestment);
            dto.setTotalCurrentValue(totalCurrentValue);
            dto.setProfitLoss(profitLoss);

            return dto;

        }).toList();
    }

}

