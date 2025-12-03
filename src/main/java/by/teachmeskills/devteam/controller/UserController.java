package by.teachmeskills.devteam.controller;

import by.teachmeskills.devteam.dto.user.UserProfileUpdateDto;
import by.teachmeskills.devteam.exception.WrongPasswordException;
import by.teachmeskills.devteam.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
            redirectAttributes.addFlashAttribute("errorMessage", "Wrong current password!");
            return "redirect:/user/userEditor";
        }
    }

    @DeleteMapping
    public String deleteUser(@AuthenticationPrincipal(expression = "id") Long userId,
                             RedirectAttributes redirectAttributes,
                             HttpServletRequest request) {
        userService.deleteById(userId);

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();

        redirectAttributes.addFlashAttribute("accountDeleted", true);

        return "redirect:/login";
    }
}
