package ru.mycrg.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.mycrg.backend.FilesDto;
import ru.mycrg.backend.FilesEntity;
import ru.mycrg.backend.repository.FileRepository;

import java.util.UUID;

@Service
public class FilesService {

    private final FilesStorageService filesStorageService;
    private final FileRepository fileRepository;

    public FilesService(FilesStorageService filesStorageService, FileRepository fileRepository) {
        this.filesStorageService = filesStorageService;
        this.fileRepository = fileRepository;
    }

    public FilesDto createFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String path = filesStorageService.copyToStorage(file, generateFileName(file));
        FilesEntity entity = new FilesEntity(file, path);
        FilesEntity savedEntity = fileRepository.save(entity);

        return new FilesDto(savedEntity);
    }

    private String generateFileName(MultipartFile file) {
        int hashCode = file.hashCode();

        return String.format("%s.%s", hashCode, UUID.randomUUID());
    }
}
