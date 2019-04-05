package by.teachmeskills.devteam.controller;

import by.teachmeskills.devteam.entity.User;
import by.teachmeskills.devteam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("/developers")
@PreAuthorize("hasAuthority('MANAGER')")
public class DeveloperController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String getDevelopers(Model model) {
        Set<User> developers = userService.getAllDevelopers();

        model.addAttribute("developers", developers);

        return "developersList";
    }

    @PostMapping
    public String updateSalary(@RequestParam Long id, @RequestParam Integer price) {

        userService.updatePrice(id, price);

        return "redirect:/developers";
    }

    @DeleteMapping
    public String deleteDeveloper(@RequestParam Long id) {
        userService.deleteById(id);
        return "redirect:/developers";
    }
}
