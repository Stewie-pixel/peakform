package com.peakform.vulnerable;

import com.peakform.model.Member;
import com.peakform.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/vuln/directory")
public class AggregationDirectoryController {

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping
    public String directory(
            @RequestParam(required = false) String suburb,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String keyword,
            Model model) {

        List<Member> all = memberRepository.findAll();

        List<Member> filtered = all.stream()
                .filter(m -> suburb == null || suburb.isBlank() || m.getSuburb().equalsIgnoreCase(suburb))
                .filter(m -> minAge == null || (m.getAge() != null && m.getAge() >= minAge))
                .filter(m -> maxAge == null || (m.getAge() != null && m.getAge() <= maxAge))
                .filter(m -> keyword == null || keyword.isBlank()
                        || (m.getMedicalNote() != null && m.getMedicalNote().toLowerCase().contains(keyword.toLowerCase())))
                .collect(Collectors.toList());

        model.addAttribute("members", filtered);
        model.addAttribute("suburb", suburb);
        model.addAttribute("minAge", minAge);
        model.addAttribute("maxAge", maxAge);
        model.addAttribute("keyword", keyword);
        return "vuln/directory";
    }
}
