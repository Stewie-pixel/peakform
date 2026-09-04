package com.peakform.secure;

import com.peakform.exception.FileValidationException;
import com.peakform.model.UploadedFile;
import com.peakform.repository.UploadedFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/secure/upload")
public class SecureUploadController {

    @Autowired
    private UploadedFileRepository fileRepository;

    @Autowired
    private FileValidationUtil fileValidationUtil;

    @Value("${peakform.upload.dir}")
    private String uploadDir;

    @GetMapping
    public String form() {
        return "secure/upload";
    }

    @PostMapping
    public String upload(@RequestParam Long memberId, @RequestParam MultipartFile file, Model model) {
        try {
            String safeFilename = fileValidationUtil.validateAndGenerateSafeFilename(file);

            File destDir = new File(uploadDir);
            if (!destDir.exists()) destDir.mkdirs();
            File dest = new File(destDir, safeFilename);
            file.transferTo(dest);

            UploadedFile record = new UploadedFile(memberId, file.getOriginalFilename(), safeFilename,
                    file.getContentType(), file.getSize());
            fileRepository.save(record);

            model.addAttribute("uploaded", record);
        } catch (FileValidationException e) {
            model.addAttribute("error", e.getMessage());
        } catch (IOException e) {
            model.addAttribute("error", "Could not store file. Please try again.");
        }

        model.addAttribute("files", fileRepository.findAllByOrderByUploadedAtDesc());
        return "secure/upload";
    }
}
