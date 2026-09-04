package com.peakform.secure;

import com.peakform.model.ProgressNote;
import com.peakform.repository.ProgressNoteRepository;
import com.peakform.secure.dto.NoteForm;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/secure/notes")
public class SecureNoteController {

    @Autowired
    private ProgressNoteRepository noteRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("notes", noteRepository.findAllByOrderByCreatedAtDesc());
        model.addAttribute("noteForm", new NoteForm());
        return "secure/notes";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("noteForm") NoteForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("notes", noteRepository.findAllByOrderByCreatedAtDesc());
            return "secure/notes";
        }
        ProgressNote note = new ProgressNote(form.getMemberId(), form.getAuthor(), form.getContent());
        noteRepository.save(note);
        return "redirect:/secure/notes";
    }
}
