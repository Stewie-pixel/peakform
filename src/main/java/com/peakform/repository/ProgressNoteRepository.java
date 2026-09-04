package com.peakform.repository;

import com.peakform.model.ProgressNote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProgressNoteRepository extends JpaRepository<ProgressNote, Long> {
    List<ProgressNote> findAllByOrderByCreatedAtDesc();
}
