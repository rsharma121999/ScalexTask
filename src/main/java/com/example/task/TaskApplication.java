package com.example.task;

import com.example.task.Entity.User;
import com.example.task.Entity.UserType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class TaskApplication {

	public static final String REGULAR_USER_FILE = "/Users/rishabh_sharma/Downloads/task/src/main/resources/static/regularUser.csv";
	public static final String ADMIN_USER_FILE = "/Users/rishabh_sharma/Downloads/task/src/main/resources/static/adminUser.csv";

	public static void main(String[] args) {
		SpringApplication.run(TaskApplication.class, args);
	}

	// Dummy user data
	public static final List<User> users = new ArrayList<>();

	static {
		users.add(new User("user1", "password1", UserType.REGULAR));
		users.add(new User("admin1", "adminpassword1", UserType.ADMIN));
	}

}


