package com.peakform.repository;

import com.peakform.model.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long> {
    List<UploadedFile> findAllByOrderByUploadedAtDesc();
}
