package com.peakform.secure;

import com.peakform.model.Member;
import com.peakform.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/secure/search")
public class SecureSearchController {

    @Autowired
    private MemberRepository memberRepository;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @GetMapping
    public String search(@RequestParam(required = false, defaultValue = "") String term, Model model) {
        List<Member> results;
        if (term.isBlank()) {
            results = List.of();
        } else if (datasourceUrl != null && datasourceUrl.contains("mysql")) {
            results = memberRepository.secureSearchByNameViaStoredProcedure(term);
        } else {
            results = memberRepository.secureSearchByName(term);
        }
        model.addAttribute("results", results);
        model.addAttribute("term", term);
        return "secure/search";
    }
}
