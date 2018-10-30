package com.mysite.controller;

import com.mysite.domain.Role;
import com.mysite.domain.User;
import com.mysite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user) {
        userService.saveUser(user, username, form);

        return "redirect:/user";
    }

    @GetMapping("profile/{user}")
    public String getProfile(Model model, @PathVariable User user) {
        model.addAttribute("eggs", user.getEggs());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("countEggs", user.getEggs().size());

        return "profile";
    }

    @PostMapping("profile/{user}")
    public String updateProfile(
            @PathVariable User user,
            @RequestParam String password,
            @RequestParam String email,
            Model model
    ) {
        model.addAttribute("countEggs", user.getEggs().size());
        model.addAttribute("eggs", user.getEggs());

        if (email == null || email.equals("")) {
            model.addAttribute("username", user.getUsername());
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "А как мы будем тебя вычислять?");

            return "profile";
        }

        boolean isPassEmpty = StringUtils.isEmpty(password);
        if (isPassEmpty) {
            model.addAttribute("username", user.getUsername());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Пароль новый/старый введи, ну!");

            return "profile";
        }

        userService.updateProfile(user, password, email);

        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("messageType", "success");
        model.addAttribute("message", "Настройки сохранены");

        return "profile";
//        return "redirect:/user/profile";
    }

    @GetMapping("subscribe/{user}")
    public String subscribe(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user
            ) {
        userService.subscribe(currentUser, user);

        return "redirect:/user-messages/" + user.getId();
    }

    @GetMapping("unsubscribe/{user}")
    public String unsubscribe(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user
            ) {
        userService.unsubscribe(currentUser, user);

        return "redirect:/user-messages/" + user.getId();
    }

    @GetMapping("{type}/{user}/list")
    public String userList(
            Model model,
            @PathVariable User user,
            @PathVariable String type
    ) {
        model.addAttribute("userChannel", user);
        model.addAttribute("type", type);

        if ("subscriptions".equals(type)) {
            model.addAttribute("users", user.getSubscriptions());
        } else {
            model.addAttribute("users", user.getSubscribers());
        }

        return "subscriptions";
    }

    @GetMapping("{type}/{currentUser}/user-messages/{user}")
    public String showSub(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user
    ) {
        return "redirect:/user-messages/" + user.getId();
    }
}
