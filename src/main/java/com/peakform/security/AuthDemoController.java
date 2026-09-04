package com.peakform.security;

import com.peakform.repository.MemberRepository;
import com.peakform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/secure-auth")
public class AuthDemoController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Model model) {
        model.addAttribute("username", principal.getName());
        return "secure-auth/dashboard";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "secure-auth/admin";
    }

    @GetMapping("/coach")
    public String coach(Model model) {
        model.addAttribute("members", memberRepository.findAll());
        return "secure-auth/coach";
    }

    @GetMapping("/client")
    public String client(Principal principal, Model model) {
        model.addAttribute("username", principal.getName());
        return "secure-auth/client";
    }

    @PostMapping("/client/settings")
    public String updateSettings(@RequestParam String displayEmail, Principal principal, Model model) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("displayEmail", displayEmail);
        model.addAttribute("updated", true);
        return "secure-auth/client";
    }
}
