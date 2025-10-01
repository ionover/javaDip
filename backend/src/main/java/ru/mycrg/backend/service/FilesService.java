package ru.mycrg.backend.service;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.mycrg.backend.dto.FilesDto;
import ru.mycrg.backend.entity.FilesEntity;
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

    public FilesDto createFile(String fileName, MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String path = filesStorageService.copyToStorage(file, generateFileName(file));
        FilesEntity entity = new FilesEntity(fileName, file, path);
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
        Pageable pageable = limit != null ? PageRequest.of(0, limit) : Pageable.unpaged();

        List<FilesEntity> filesEntities = fileRepository.findAllNotDeleted(pageable);

        return filesEntities.stream()
                            .map(FilesDto::new)
                            .toList();
    }

    public FilesEntity getFileEntityByName(String filename) {
        return fileRepository.findByTitleAndNotDeleted(filename)
                             .orElseThrow(() -> new IllegalArgumentException("File not found: " + filename));
    }

    public Resource getFileResource(String path) {
        return filesStorageService.loadFileAsResource(path);
    }

    public void updateFileName(String currentFileName, String newFileName) {
        FilesEntity entity = fileRepository.findByTitleAndNotDeleted(currentFileName)
                                           .orElseThrow(() -> new IllegalArgumentException("File not found: " + currentFileName));
        
        entity.setTitle(newFileName);
        fileRepository.save(entity);
    }
}
