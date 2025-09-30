package ru.mycrg.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.mycrg.backend.FilesDto;
import ru.mycrg.backend.FilesEntity;
import ru.mycrg.backend.repository.FileRepository;
import ru.mycrg.backend.util.FileUtils;

import java.util.List;
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
        String extension = FileUtils.extractExtension(file.getOriginalFilename());

        if (extension != null && !extension.isEmpty()) {
            return String.format("%s_%s.%s", hashCode, UUID.randomUUID(), extension);
        } else {
            return String.format("%s_%s", hashCode, UUID.randomUUID());
        }
    }

    public void deleteFile(UUID id) {
        FilesEntity entity = fileRepository.findById(id)
                                           .orElseThrow(
                                                   () -> new IllegalArgumentException("File not found with id: " + id));

        entity.setIsDeleted(true);
        fileRepository.save(entity);
    }

    public List<FilesDto> getAllWithLimit(Integer limit) {
        List<FilesEntity> filesEntities = fileRepository.getAllWithLimit(limit);

        return filesDtoList;
    }
}
