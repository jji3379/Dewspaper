package backend.ssr.ddd.ssrblog.controller;

import backend.ssr.ddd.ssrblog.oauth.SessionAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class LoginTestController {

    private final HttpSession httpSession;

    @GetMapping("/dddLogin")
    public String index(Model model) {
        SessionAccount user = (SessionAccount) httpSession.getAttribute("account");

        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "index";
    }
}
