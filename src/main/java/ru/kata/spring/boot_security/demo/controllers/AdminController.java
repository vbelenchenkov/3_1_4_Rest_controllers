package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //List of all users (only under admin password)
    @GetMapping
    public String listUser(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    //New user form
    @GetMapping("/new")
    public String newUSerForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleRepository.findAll());
        return "admin/user-form";
    }

    //Saving new user
    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user,
                           @RequestParam("roles") List<Integer> roleIds) {
        //password encrypting
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //roles setting
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));
        user.setRoles(roles);
        userRepository.save(user);
        return "redirect:/admin";
    }

    //User editing form
    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Integer id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleRepository.findAll());
        return "admin/user-form";
    }

    //user updating
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Integer id,
                             @ModelAttribute User user,
                             @RequestParam("roles") List<Integer> roleIds) {
        User exitingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        //field updating, except password
        exitingUser.setName(user.getName());
        exitingUser.setEmail(user.getEmail());
        exitingUser.setAge(user.getAge());

        //if password isn't empty - encrypt and save
        if(user.getPassword() != null && !user.getPassword().isEmpty()) {
            exitingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        //roles updating
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));
        exitingUser.setRoles(roles);

        userRepository.save(exitingUser);
        return "redirect:/admin";
    }

    //user deletion
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userRepository.deleteById(id);
        return "redirect:/admin";
    }
}
