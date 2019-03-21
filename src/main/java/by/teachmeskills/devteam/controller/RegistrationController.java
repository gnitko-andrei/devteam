package by.teachmeskills.devteam.controller;

import by.teachmeskills.devteam.entity.Role;
import by.teachmeskills.devteam.entity.User;
import by.teachmeskills.devteam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
public class RegistrationController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, @RequestParam Map<String, String> formRoles, Model model) {
        User userFromDb = userRepository.findByUsername(user.getUsername());

        if (userFromDb != null) {
            model.addAttribute("message", "User already exists!");
            return "registration";
        }
        System.out.println(formRoles);
        user.setActive(true);
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        if (formRoles.get("inlineRadioOptions").equals("option1")) {
            roles.add(Role.CUSTOMER);
        }
        if (formRoles.get("inlineRadioOptions").equals("option2")) {
            roles.add(Role.MANAGER);
        }
        if (formRoles.get("inlineRadioOptions").equals("option3")) {
            roles.add(Role.DEVELOPER);
        }
        user.setRoles(roles);
        userRepository.save(user);

        return "redirect:/login";
    }
}
