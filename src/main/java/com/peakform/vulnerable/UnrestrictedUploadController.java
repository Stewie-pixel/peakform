package com.peakform.vulnerable;

import com.peakform.model.UploadedFile;
import com.peakform.repository.UploadedFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
@RequestMapping("/vuln/upload")
public class UnrestrictedUploadController {

    @Autowired
    private UploadedFileRepository fileRepository;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @GetMapping
    public String form() {
        return "vuln/upload";
    }

    @PostMapping
    public String upload(@RequestParam Long memberId, @RequestParam MultipartFile file, Model model) throws IOException {
        String originalFilename = file.getOriginalFilename();

        File dest = new File(UPLOAD_DIR + originalFilename);
        dest.getParentFile().mkdirs();
        file.transferTo(dest);

        UploadedFile record = new UploadedFile(memberId, originalFilename, originalFilename,
                file.getContentType(), file.getSize());
        fileRepository.save(record);

        model.addAttribute("uploaded", record);
        model.addAttribute("files", fileRepository.findAllByOrderByUploadedAtDesc());
        return "vuln/upload";
    }
}
