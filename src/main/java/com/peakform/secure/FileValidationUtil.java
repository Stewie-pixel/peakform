package com.peakform.secure;

import com.peakform.exception.FileValidationException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component
public class FileValidationUtil {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "pdf");
    private static final long MAX_SIZE_BYTES = 2L * 1024 * 1024;

    private static final Map<String, byte[]> MAGIC_BYTES = Map.of(
            "png", new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47},
            "jpg", new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF},
            "jpeg", new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF},
            "pdf", new byte[]{0x25, 0x50, 0x44, 0x46}
    );

    public String validateAndGenerateSafeFilename(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new FileValidationException("Uploaded file is empty.");
        }
        if (file.getSize() > MAX_SIZE_BYTES) {
            throw new FileValidationException("File exceeds the 2 MB size limit.");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new FileValidationException("File must have a valid extension.");
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new FileValidationException("File type ." + extension + " is not permitted. Allowed: " + ALLOWED_EXTENSIONS);
        }

        byte[] header = new byte[4];
        try (var in = file.getInputStream()) {
            int read = in.read(header);
            if (read < 4) {
                throw new FileValidationException("File is too small to validate.");
            }
        }
        byte[] expected = MAGIC_BYTES.get(extension);
        for (int i = 0; i < expected.length; i++) {
            if (header[i] != expected[i]) {
                throw new FileValidationException(
                        "File content does not match its extension (expected " + extension.toUpperCase() + " signature).");
            }
        }

        return UUID.randomUUID() + "." + extension;
    }
}
