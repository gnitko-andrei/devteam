package by.teachmeskills.devteam.controller;

import by.teachmeskills.devteam.dto.user.UserRegistrationDto;
import by.teachmeskills.devteam.entity.Role;
import by.teachmeskills.devteam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @GetMapping("/registration")
    public String getRegistrationPage() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute UserRegistrationDto user,
                          @RequestParam Role userRole,
                          RedirectAttributes redirectAttributes) {
        if(userService.isUserExists(user.getUsername())) {
            redirectAttributes.addFlashAttribute("errorMessage", "User already exists!");
            return "redirect:/registration";
        } else {
            userService.createNewUser(user, userRole);
            return "redirect:/login";
        }
    }
}
