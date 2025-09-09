package by.teachmeskills.devteam.controller;

import by.teachmeskills.devteam.dto.user.UserProfileUpdateDto;
import by.teachmeskills.devteam.exception.WrongPasswordException;
import by.teachmeskills.devteam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public String getUserInfo(@AuthenticationPrincipal(expression = "id") Long userId,
                              Model model) {
        var userData = userService.findById(userId);

        model.addAttribute("currentUser", userData);

        return "userProfile";
    }

    @GetMapping("/userEditor")
    public String getUserEditor(@AuthenticationPrincipal(expression = "id") Long userId,
                                Model model) {
        var userData = userService.findById(userId);

        model.addAttribute("user", userData);

        return "userEditor";
    }

    @PostMapping("/userEditor")
    public String updateUserData(@AuthenticationPrincipal(expression = "id") Long userId,
                                 @ModelAttribute UserProfileUpdateDto userProfileUpdateData,
                                 RedirectAttributes redirectAttributes) {
        try {
            userService.updateUserProfile(userId, userProfileUpdateData);
            return "redirect:/user";
        } catch (WrongPasswordException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Неверный текущий пароль!");
            return "redirect:/user/userEditor";
        }
    }

    @DeleteMapping
    public String deleteUser(@AuthenticationPrincipal(expression = "id") Long userId) {
        userService.deleteById(userId);
        return "redirect:/logout";
    }
}
