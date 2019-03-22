package by.teachmeskills.devteam.controller;

import by.teachmeskills.devteam.entity.User;
import by.teachmeskills.devteam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String userInfo(Model model, @AuthenticationPrincipal User user) {
        String userAppRole = userService.getUserAppRoleName(user);
        model.addAttribute("user", user);
        model.addAttribute("appRole", userAppRole);
        return "userProfile";
    }

}
