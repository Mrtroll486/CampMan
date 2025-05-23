package org.guan.campman.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    /* Todo: note that this controller is just for testing spring security, use RestController in future */
    @GetMapping("/")
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/testAuth")
    public String testAuth(Model model) {
        return "testAuth";
    }
}
