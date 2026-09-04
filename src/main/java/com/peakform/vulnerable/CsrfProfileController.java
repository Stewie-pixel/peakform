package com.peakform.vulnerable;

import com.peakform.model.Member;
import com.peakform.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vuln/profile")
public class CsrfProfileController {

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/{id}")
    public String form(@PathVariable Long id, Model model) {
        model.addAttribute("member", memberRepository.findById(id).orElseThrow());
        return "vuln/profile";
    }

    @PostMapping("/update")
    public String update(@RequestParam Long id, @RequestParam String email, @RequestParam String availableSlot, Model model) {
        Member member = memberRepository.findById(id).orElseThrow();
        member.setEmail(email);
        member.setAvailableSlot(availableSlot);
        memberRepository.save(member);
        model.addAttribute("member", member);
        model.addAttribute("updated", true);
        return "vuln/profile";
    }
}
