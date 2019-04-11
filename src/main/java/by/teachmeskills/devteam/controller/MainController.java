package by.teachmeskills.devteam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String home(Model model) {

        return "home";
    }

    @GetMapping("/info/project")
    public String projectInfo(Model model) {

        return "homeMoreInfo";
    }

    @GetMapping("/info/stack")
    public String stackInfo(Model model) {

        return "stackMoreInfo";
    }
}
