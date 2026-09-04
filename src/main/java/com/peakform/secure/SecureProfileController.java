package com.peakform.secure;

import com.peakform.model.Member;
import com.peakform.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/secure/profile")
public class SecureProfileController {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CustomCsrfService csrfService;

    @GetMapping("/{id}")
    public String form(@PathVariable Long id, HttpServletRequest request, Model model) {
        model.addAttribute("member", memberRepository.findById(id).orElseThrow());
        model.addAttribute("csrfToken", csrfService.getOrCreateToken(request));
        return "secure/profile";
    }

    @PostMapping("/update")
    public String update(@RequestParam Long id,
                          @RequestParam String email,
                          @RequestParam String availableSlot,
                          @RequestParam("csrfToken") String submittedToken,
                          HttpServletRequest request,
                          Model model) {

        boolean tokenOk = csrfService.isTokenValid(request, submittedToken);
        boolean refererOk = csrfService.isRefererValid(request);

        if (!tokenOk || !refererOk) {
            model.addAttribute("error", "Request rejected: CSRF validation failed "
                    + "(token valid=" + tokenOk + ", referer valid=" + refererOk + ").");
            model.addAttribute("member", memberRepository.findById(id).orElseThrow());
            model.addAttribute("csrfToken", csrfService.getOrCreateToken(request));
            return "secure/profile";
        }

        Member member = memberRepository.findById(id).orElseThrow();
        member.setEmail(email);
        member.setAvailableSlot(availableSlot);
        memberRepository.save(member);

        model.addAttribute("member", member);
        model.addAttribute("updated", true);
        model.addAttribute("csrfToken", csrfService.getOrCreateToken(request));
        return "secure/profile";
    }
}
