package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.findUserById(id)
                .orElseThrow(() -> new RuntimeException("User with id: " + id + " not found"));
    }

    @GetMapping("/roles")
    public List<Role> getAllRoles(){
        return  roleService.findAllRoles();
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user,
                           @RequestParam List<Integer> roleIds) {
        return userService.createUser(user, roleIds);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Integer id,
                           @RequestBody User user,
                           @RequestParam List<Integer> roleIds){
        return userService.updateUser(id, user, roleIds);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }
}

