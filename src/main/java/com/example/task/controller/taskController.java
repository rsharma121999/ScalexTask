package com.example.task.controller;

import com.example.task.Entity.User;
import com.example.task.Entity.UserType;
import com.example.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.example.task.TaskApplication.*;

@RestController
public class taskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword())) {
                // Generate and return JWT token
                String token = taskService.generateJWTToken(user.getUsername());
                return ResponseEntity.ok(token);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }

    @GetMapping("/home")
    public ResponseEntity<List<String>> getBooks(@RequestHeader("Authorization") String token) {
        // Validate token
        if (!taskService.validateJWTToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Read books based on user type
        List<String> books = new ArrayList<>();
        for (User user : users) {
            if (user.getUsername().equals(taskService.getUsernameFromToken(token))) {
                if (user.getUserType() == UserType.REGULAR) {
                    books.addAll(taskService.readBooksFromFile(REGULAR_USER_FILE));
                } else if (user.getUserType() == UserType.ADMIN) {
                    books.addAll(taskService.readBooksFromFile(REGULAR_USER_FILE));
                    books.addAll(taskService.readBooksFromFile(ADMIN_USER_FILE));
                }
                return ResponseEntity.ok(books);
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping("/addBook")
    public ResponseEntity<String> addBook(@RequestHeader("Authorization") String token,
                                          @RequestParam String bookName,
                                          @RequestParam String author,
                                          @RequestParam int publicationYear) {
        // Validate token
        if (!taskService.validateJWTToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Check if user is admin
        if (!taskService.isUserAdmin(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admin users can access this endpoint");
        }

        // Validate parameters
        if (!taskService.isValidString(bookName) || !taskService.isValidString(author) || publicationYear <= 0) {
            return ResponseEntity.badRequest().body("Invalid parameters");
        }

        // Add book to regularUser.csv
        taskService.addBookToFile(REGULAR_USER_FILE, bookName + "," + author + "," + publicationYear);

        return ResponseEntity.ok("Book added successfully");
    }

    @DeleteMapping("/deleteBook")
    public ResponseEntity<String> deleteBook(@RequestHeader("Authorization") String token,
                                             @RequestParam String bookName) {
        // Validate token
        if (!taskService.validateJWTToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Check if user is admin
        if (!taskService.isUserAdmin(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admin users can access this endpoint");
        }

        // Validate parameter
        if (!taskService.isValidString(bookName)) {
            return ResponseEntity.badRequest().body("Invalid parameter");
        }

        // Delete book from regularUser.csv
        taskService.deleteBookFromFile(REGULAR_USER_FILE, bookName);

        return ResponseEntity.ok("Book deleted successfully");
    }
}
