package ru.mycrg.backend.util;

import org.springframework.stereotype.Component;

@Component
public class FileUtils {
    
    public static String extractExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return null;
        }
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex + 1);
        }
        return null;
    }
}