package by.teachmeskills.devteam.controller;

import by.teachmeskills.devteam.dto.user.UserUpdateByAdminDto;
import by.teachmeskills.devteam.entity.Role;
import by.teachmeskills.devteam.exception.UsernameAlreadyInUseException;
import by.teachmeskills.devteam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping
    public String getUsersList(Model model) {
        model.addAttribute("usersList", userService.findAll());
        return "adminPanel";
    }

    @GetMapping("{userId}/edit")
    public String getUserEditor(@PathVariable Long userId, Model model) {
        var user = userService.findById(userId);
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "adminUserEditor";
    }

    @PostMapping("{userId}/edit")
    public String updateUserData(@PathVariable Long userId,
                                 @ModelAttribute UserUpdateByAdminDto userUpdateData,
                                 RedirectAttributes redirectAttributes) {
        try {
            userService.updateUserByAdmin(userId, userUpdateData);
            return "redirect:/admin";
        } catch (UsernameAlreadyInUseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/{userId}/edit";
        }
    }
}
