package com.peakform.secure;

import com.peakform.exception.ResourceNotFoundException;
import com.peakform.model.Member;
import com.peakform.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/secure/member")
public class MemberLookupController {

    private static final Logger log = LoggerFactory.getLogger(MemberLookupController.class);

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No member with id " + id));
        model.addAttribute("member", member);
        return "secure/member-detail";
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleMemberNotFound(ResourceNotFoundException ex, Model model) {
        log.warn("Member lookup failed: {}", ex.getMessage());
        model.addAttribute("message", ex.getMessage());
        return "secure/member-not-found";
    }
}
