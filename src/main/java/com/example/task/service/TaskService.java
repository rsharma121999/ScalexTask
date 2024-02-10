package com.example.task.service;

import com.example.task.Entity.User;
import com.example.task.Entity.UserType;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.task.TaskApplication.users;

@Service
public class TaskService {

    public boolean validateJWTToken(String token) {
        // Dummy implementation for token validation
        return token != null && token.startsWith("Bearer");
    }

    public String generateJWTToken(String username) {
        // Dummy implementation for token generation
        return "Bearer " + username + "-jwt-token";
    }

    public String getUsernameFromToken(String token) {
        // Dummy implementation to extract username from token
        return token.substring(7); // Remove "Bearer "
    }

    public boolean isUserAdmin(String token) {
        // Dummy implementation to check if user is admin based on token
        String username = getUsernameFromToken(token);
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getUserType() == UserType.ADMIN) {
                return true;
            }
        }
        return false;
    }

    public List<String> readBooksFromFile(String fileName) {
        List<String> books = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                books.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return books;
    }

    public void addBookToFile(String fileName, String bookData) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(bookData);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteBookFromFile(String fileName, String bookName) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.toLowerCase().startsWith(bookName.toLowerCase())) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isValidString(String str) {
        return str != null && !str.isEmpty();
    }
}
