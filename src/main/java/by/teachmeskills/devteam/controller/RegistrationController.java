package by.teachmeskills.devteam.controller;

import by.teachmeskills.devteam.entity.User;
import by.teachmeskills.devteam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, @RequestParam Map<String, String> formRoles, Model model) {
        User userFromDb = userService.findByUsername(user.getUsername());

        if (userFromDb != null) {
            model.addAttribute("message", "User already exists!");
            return "registration";
        }
        user.setPrice(3);
        userService.saveNewUser(user, formRoles);

        return "redirect:/login";
    }
}
