package by.teachmeskills.devteam.controller;

import by.teachmeskills.devteam.entity.User;
import by.teachmeskills.devteam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String userInfo(Model model, @AuthenticationPrincipal User user) {
        String userAppRole = userService.getUserAppRoleName(user);
        model.addAttribute("user", user);
        model.addAttribute("appRole", userAppRole);
        return "userProfile";
    }

    @GetMapping("/userEditor")
    public String editUserForm(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        return "userEditor";
    }

    @PostMapping
    public String updateProfile(@AuthenticationPrincipal User user,
                                @RequestParam Map<String, String> formData,
                                Model model) {
        boolean status = userService.updateProfile(user, formData);
        if (!status) {
            model.addAttribute("user", user);
            model.addAttribute("message", "Неверный текущий пароль!");
            return "userEditor";
        }


        return "redirect:/user";
    }

    @DeleteMapping
    public String deleteUser(@AuthenticationPrincipal User user) {
        userService.delete(user);
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        return "redirect:/login";
    }


}
