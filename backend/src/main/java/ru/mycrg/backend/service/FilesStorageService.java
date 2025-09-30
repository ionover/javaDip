package ru.mycrg.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.mycrg.backend.dto.FilesDto;
import ru.mycrg.backend.exception.FilesException;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
public class FilesStorageService {

    private static final Logger log = LoggerFactory.getLogger(FilesStorageService.class);

    private final Path mainStoragePath;

    @Autowired
    public FilesStorageService(Environment environment) {
        String storagePath = environment.getRequiredProperty("back-options.filesPath");

        this.mainStoragePath = Paths.get(storagePath).toAbsolutePath().normalize();

        // Создаем директорию для хранения файлов, если она не существует
        try {
            Files.createDirectories(this.mainStoragePath);
        } catch (IOException e) {
            log.error("Не удалось создать директорию для хранения файлов: {}", this.mainStoragePath, e);
            throw new FilesException("Не удалось создать директорию для хранения файлов");
        }
    }

    public String copyToStorage(MultipartFile file, String fileName) {
        return copyTo(file, mainStoragePath, fileName);
    }

    private String copyTo(MultipartFile file, Path storagePath, String fileName) {
        Path targetLocation = null;
        try {
            targetLocation = storagePath.resolve(fileName);

            Files.copy(file.getInputStream(), targetLocation, REPLACE_EXISTING);

            return targetLocation.normalize().toString();
        } catch (AccessDeniedException e) {
            String msg = "Нет доступа на сохранение файла, по пути: " + targetLocation;
            log.debug(msg, e);

            throw new FilesException(msg);
        } catch (Exception e) {
            String msg = "Не удалось сохранить файл, по пути: " + targetLocation;
            log.debug(msg, e);

            throw new FilesException(msg);
        }
    }

    public List<FilesDto> getAllWithLimit(Integer limit) {
        return null;
    }
}
