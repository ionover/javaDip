package ru.mycrg.backend.unit;

import org.junit.jupiter.api.Test;
import ru.mycrg.backend.util.FileUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FileUtilsTests {

    @Test
    void testExtractExtension_WithValidExtension() {
        String result = FileUtils.extractExtension("document.pdf");
        assertEquals("pdf", result);
    }

    @Test
    void testExtractExtension_WithMultipleDots() {
        String result = FileUtils.extractExtension("archive.tar.gz");
        assertEquals("gz", result);
    }

    @Test
    void testExtractExtension_WithoutExtension() {
        String result = FileUtils.extractExtension("filename");
        assertNull(result);
    }

    @Test
    void testExtractExtension_WithEmptyString() {
        String result = FileUtils.extractExtension("");
        assertNull(result);
    }

    @Test
    void testExtractExtension_WithDotAtEnd() {
        String result = FileUtils.extractExtension("filename.");
        assertNull(result);
    }
}
