package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestRestController {

    private final UserService userService;

    public TestRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/hello")
    public Map<String, String> sayHello() {

        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello world from REST!");
        response.put("status", "OK");
        response.put("information", "First REST API");
        response.put("group", "test");

        return response;
    }

    @GetMapping("/users")
    public List<User> getUser() {
        return userService.findAllUsers();
    }

    @GetMapping("/test-users")
    public List<User> testUsers() {
        List<User> users = userService.findAllUsers();
        System.out.println("=== TEST USERS ===");
        System.out.println("Count: " + users.size());
        users.forEach(user -> {
            System.out.println("User: " + user.getEmail());
            System.out.println("Roles size: " + user.getRoles().size());
        });
        return users;
    }
}
