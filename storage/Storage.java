package com.BankingApp.storage;

import com.BankingApp.model.BankAccount;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Storage {

    public static void saveAccounts(Map<Integer, BankAccount> accounts, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (BankAccount account : accounts.values()) {
                writer.printf("%d,%s,%.2f%n",
                        account.getAccountNumber(),
                        account.getPassword(),
                        account.getBalance());
            }
        } catch (IOException e) {
            System.out.println("Error saving " + filename + ": " + e.getMessage());
        }
    }

    public static Map<Integer, BankAccount> loadAccounts(String filename) {
        Map<Integer, BankAccount> accounts = new HashMap<>();
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("No file found: " + filename + "; Creating new one...");
            return accounts;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 3) continue;

                int accountNumber = Integer.parseInt(parts[0].trim());
                String password = parts[1].trim();
                float balance = Float.parseFloat(parts[2].trim());

                BankAccount account = new BankAccount(accountNumber, password, balance);
                accounts.put(accountNumber, account);
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading " + filename + ": " + e.getMessage());
        }

        return accounts;
    }

//    public static <T> T load(String filename) {
//        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
//            return (T) in.readObject(); // should be checked to avoid errors, but for now it only uses internal files
//
////            Map<Integer, BankAccount> obj = (Map<Integer, BankAccount>) in.readObject();
////            if(obj instanceof Map<Integer, BankAccount>) {
////                for(Map.Entry<Integer, BankAccount> entry : obj.entrySet()) {
////                    System.out.print("Account nr." + entry.getValue().getAccountNumber() + " balance is: ");
////                    entry.getValue().printBalance();
////                }
////            }
//
//        } catch (FileNotFoundException e) {
//            System.out.println("No file found: " + filename + "; Creating new one...");
//            return null;
//        } catch (IOException | ClassNotFoundException e) {
//            System.out.println("Error loading " + filename + ": " + e.getMessage());
//            return null;
//        }
//    }
//
//    public static <T> void save(T data, String filename) {
//        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
//            out.writeObject(data);
//        } catch (IOException e) {
//            System.out.println("Error saving " + filename + ": " + e.getMessage());
//        }
//    }
}
