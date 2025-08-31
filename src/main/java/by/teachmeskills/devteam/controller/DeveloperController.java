package by.teachmeskills.devteam.controller;

import by.teachmeskills.devteam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/developers")
@PreAuthorize("hasAuthority('MANAGER')")
@RequiredArgsConstructor
public class DeveloperController {

    private final UserService userService;

    @GetMapping
    public String getDevelopers(Model model) {
        var developers = userService.getAllDevelopers();

        model.addAttribute("developers", developers);

        return "developersList";
    }

    @PostMapping
    public String updateSalary(@RequestParam Long id, @RequestParam Integer price) {
        userService.updateDeveloperRate(id, price);

        return "redirect:/developers";
    }

    @DeleteMapping
    public String deleteDeveloper(@RequestParam Long id) {
        userService.deleteById(id);

        return "redirect:/developers";
    }
}
