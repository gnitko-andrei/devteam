package by.teachmeskills.devteam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String getHomePage() {
        return "home";
    }

    @GetMapping("/info/project")
    public String getProjectInfoPage() {
        return "homeMoreInfo";
    }

    @GetMapping("/info/stack")
    public String getProjectStackInfo() {
        return "stackMoreInfo";
    }
}
