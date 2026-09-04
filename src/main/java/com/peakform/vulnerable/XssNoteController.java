package com.peakform.vulnerable;

import com.peakform.model.ProgressNote;
import com.peakform.repository.ProgressNoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vuln/notes")
public class XssNoteController {

    @Autowired
    private ProgressNoteRepository noteRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("notes", noteRepository.findAllByOrderByCreatedAtDesc());
        model.addAttribute("newNote", new ProgressNote());
        return "vuln/notes";
    }

    @PostMapping("/add")
    public String add(@RequestParam String author, @RequestParam String content, @RequestParam Long memberId) {
        ProgressNote note = new ProgressNote(memberId, author, content);
        noteRepository.save(note);
        return "redirect:/vuln/notes";
    }
}
