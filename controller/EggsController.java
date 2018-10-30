package com.mysite.controller;

import com.mysite.domain.Egg;
import com.mysite.domain.User;
import com.mysite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EggsController {
    @Autowired
    UserService userService;

    @PostMapping("/eggs1/{user}")
    public String egg1(
            @PathVariable User user,
            Model model
    ) {
        checkEggsAndAddIt(model, user, Egg.EGG1);
        return "eggs/eggs1";
    }

    @PostMapping("/eggs2/{user}")
    public String egg2(
            @PathVariable User user,
            Model model
    ) {
        checkEggsAndAddIt(model, user, Egg.EGG2);
        return "eggs/eggs2";
    }

    @PostMapping("/eggs3/{user}")
    public String egg3(
            @PathVariable User user,
            Model model
    ) {
        checkEggsAndAddIt(model, user, Egg.EGG3);
        return "eggs/eggs3";
    }

    @PostMapping("/eggs4/{user}")
    public String egg4(
            @PathVariable User user,
            Model model
    ) {
        checkEggsAndAddIt(model, user, Egg.EGG4);
        return "eggs/eggs4";
    }

    private void checkEggsAndAddIt(Model model, User user, Egg egg) {
        if (userService.addEgg(user, egg)) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "Поздравляем, Вы нашли пасхалку!");
        } else {
            model.addAttribute("messageType", "secondary");
            model.addAttribute("message", "Эта пасхалка уже вскрыта Вами");
        }
    }
}
