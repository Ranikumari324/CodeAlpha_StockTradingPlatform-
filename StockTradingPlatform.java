import java.util.*;
import java.io.*;

/*
 * TASK 2: Stock Trading Platform
 * Java Console Application
 * Internship Ready Project
 */

// Stock class
class Stock {
    String symbol;
    double price;

    Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }
}

// User Portfolio class
class Portfolio {
    HashMap<String, Integer> holdings = new HashMap<>();

    void buyStock(String symbol, int quantity) {
        holdings.put(symbol, holdings.getOrDefault(symbol, 0) + quantity);
    }

    void sellStock(String symbol, int quantity) {
        if (holdings.containsKey(symbol)) {
            int currentQty = holdings.get(symbol);
            if (currentQty >= quantity) {
                holdings.put(symbol, currentQty - quantity);
                if (holdings.get(symbol) == 0) {
                    holdings.remove(symbol);
                }
            }
        }
    }

    void displayPortfolio(Map<String, Stock> market) {
        System.out.println("\n----- Your Portfolio -----");
        double totalValue = 0;

        for (String symbol : holdings.keySet()) {
            int qty = holdings.get(symbol);
            double price = market.get(symbol).price;
            double value = qty * price;
            totalValue += value;

            System.out.println(symbol + " | Qty: " + qty + " | Value: " + value);
        }

        System.out.println("Total Portfolio Value: " + totalValue);
    }
}

public class StockTradingPlatform {

    static Scanner sc = new Scanner(System.in);
    static Map<String, Stock> market = new HashMap<>();
    static Portfolio portfolio = new Portfolio();

    public static void main(String[] args) {

        loadMarketData();
        loadPortfolio();

        int choice;
        do {
            System.out.println("\n===== STOCK TRADING PLATFORM =====");
            System.out.println("1. View Market Data");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    displayMarket();
                    break;
                case 2:
                    buyStock();
                    break;
                case 3:
                    sellStock();
                    break;
                case 4:
                    portfolio.displayPortfolio(market);
                    break;
                case 5:
                    savePortfolio();
                    System.out.println("Exiting platform...");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 5);
    }

    // Market Data
    static void loadMarketData() {
        market.put("AAPL", new Stock("AAPL", 150.0));
        market.put("GOOG", new Stock("GOOG", 2800.0));
        market.put("TSLA", new Stock("TSLA", 700.0));
        market.put("AMZN", new Stock("AMZN", 3300.0));
    }

    static void displayMarket() {
        System.out.println("\n----- Market Data -----");
        for (Stock stock : market.values()) {
            System.out.println(stock.symbol + " : $" + stock.price);
        }
    }

    static void buyStock() {
        displayMarket();
        System.out.print("Enter stock symbol: ");
        String symbol = sc.next();

        if (!market.containsKey(symbol)) {
            System.out.println("Stock not found!");
            return;
        }

        System.out.print("Enter quantity: ");
        int qty = sc.nextInt();

        portfolio.buyStock(symbol, qty);
        System.out.println("Stock purchased successfully!");
    }

    static void sellStock() {
        System.out.print("Enter stock symbol: ");
        String symbol = sc.next();

        System.out.print("Enter quantity: ");
        int qty = sc.nextInt();

        portfolio.sellStock(symbol, qty);
        System.out.println("Stock sold successfully!");
    }

    // File I/O
    static void savePortfolio() {
        try {
            FileWriter fw = new FileWriter("portfolio.txt");
            for (String symbol : portfolio.holdings.keySet()) {
                fw.write(symbol + "," + portfolio.holdings.get(symbol) + "\n");
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("Error saving portfolio.");
        }
    }

    static void loadPortfolio() {
        try {
            File file = new File("portfolio.txt");
            if (!file.exists()) return;

            Scanner fileSc = new Scanner(file);
            while (fileSc.hasNextLine()) {
                String[] data = fileSc.nextLine().split(",");
                portfolio.holdings.put(data[0], Integer.parseInt(data[1]));
            }
            fileSc.close();
        } catch (Exception e) {
            System.out.println("Error loading portfolio.");
        }
    }
}
