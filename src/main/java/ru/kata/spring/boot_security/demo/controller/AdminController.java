package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final UserValidator userValidator;

    @Autowired
    public AdminController(UserService userService, UserValidator userValidator, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidator = userValidator;
    }

    @GetMapping
    public String allUsers(Principal principal, Model model) {
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("allUsers", allUsers);
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("adminUser", user);
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("roles", roles);
        return "all_users";
    }

    @GetMapping("/addNewUser")
    public String addNewUser(Principal principal, Model model) {
        User admin = userService.findByUsername(principal.getName());
        model.addAttribute("adminUser", admin);
        User user = new User();
        model.addAttribute("user", user);
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("roles", roles);
        return "add-new-user";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, @RequestParam("role") String roleName, Model model, Principal principal) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            User admin = userService.findByUsername(principal.getName());
            model.addAttribute("adminUser", admin);
            List<Role> roles = roleService.getAllRoles();
            model.addAttribute("roles", roles);
            return "add-new-user";
        }
        userService.saveUser(user, roleName);
        return "redirect:/admin";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
