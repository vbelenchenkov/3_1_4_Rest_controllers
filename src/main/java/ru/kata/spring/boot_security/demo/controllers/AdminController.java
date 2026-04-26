package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String getUsersList(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("allRoles", roleService.findAllRoles());
        return "admin/users";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user, @RequestParam("roles") List<Integer> roleIds) {
        userService.createUser(user, roleIds);
        return "redirect:/admin";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Integer id,
                             @ModelAttribute User user,
                             @RequestParam("roles") List<Integer> roleIds) {
        userService.updateUser(id, user, roleIds);
        return "redirect:/admin";
    }

    @PostMapping("/update")
    public String updateUserFromModal(@RequestParam Integer id,
                                      @RequestParam String firstName,
                                      @RequestParam String lastName,
                                      @RequestParam Integer age,
                                      @RequestParam String email,
                                      @RequestParam(required = false) String password,
                                      @RequestParam(required = false) List<Integer> roles) {

        userService.updateUserFromModal(id, firstName, lastName, age, email, password, roles);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam Integer id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/current-user")
    public User getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));
    }
}