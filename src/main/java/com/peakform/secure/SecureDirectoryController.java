package com.peakform.secure;

import com.peakform.model.Member;
import com.peakform.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/secure/directory")
public class SecureDirectoryController {

    private static final Logger log = LoggerFactory.getLogger(SecureDirectoryController.class);

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RateLimiter rateLimiter;

    @GetMapping
    public String directory(
            @RequestParam(required = false) String suburb,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            HttpServletRequest request,
            Principal principal,
            Model model) {

        String who = principal != null ? principal.getName() : "unknown";
        log.info("Directory query by user='{}' suburb='{}' minAge={} maxAge={} sessionId={}",
                who, suburb, minAge, maxAge, request.getSession().getId());

        if (!rateLimiter.allowRequest(request.getSession().getId())) {
            model.addAttribute("error", "Too many directory queries. Please wait a minute and try again.");
            model.addAttribute("members", List.of());
            return "secure/directory";
        }

        List<Member> all = memberRepository.findAll();
        List<Map<String, Object>> results = all.stream()
                .filter(m -> suburb == null || suburb.isBlank() || m.getSuburb().equalsIgnoreCase(suburb))
                .filter(m -> minAge == null || (m.getAge() != null && m.getAge() >= minAge))
                .filter(m -> maxAge == null || (m.getAge() != null && m.getAge() <= maxAge))
                .map(this::toGeneralizedView)
                .collect(Collectors.toList());

        model.addAttribute("members", results);
        model.addAttribute("suburb", suburb);
        model.addAttribute("minAge", minAge);
        model.addAttribute("maxAge", maxAge);
        return "secure/directory";
    }

    private Map<String, Object> toGeneralizedView(Member m) {
        Map<String, Object> view = new HashMap<>();
        view.put("fullName", m.getFullName());
        view.put("suburb", m.getSuburb());
        view.put("ageRange", generalizeAge(m.getAge()));
        view.put("dayOnly", generalizeSlotToDayOnly(m.getAvailableSlot()));
        return view;
    }

    private String generalizeAge(Integer age) {
        if (age == null) return "unknown";
        int bucketStart = (age / 10) * 10;
        return bucketStart + "-" + (bucketStart + 9);
    }

    private String generalizeSlotToDayOnly(String availableSlot) {
        if (availableSlot == null || availableSlot.isBlank()) return "unknown";
        return availableSlot.split(" ")[0].replace(",", "");
    }
}
