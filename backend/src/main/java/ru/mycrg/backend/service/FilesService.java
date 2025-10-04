package ru.mycrg.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.mycrg.backend.dto.response.FilesDto;
import ru.mycrg.backend.entity.FilesEntity;
import ru.mycrg.backend.exception.FilesException;
import ru.mycrg.backend.exception.InvalidInputDataException;
import ru.mycrg.backend.repository.FileRepository;
import ru.mycrg.backend.util.FileUtils;

import java.util.List;
import java.util.UUID;

@Service
public class FilesService {

    private static final Logger log = LoggerFactory.getLogger(FilesService.class);

    private final FilesStorageService filesStorageService;
    private final FileRepository fileRepository;

    public FilesService(FilesStorageService filesStorageService, FileRepository fileRepository) {
        this.filesStorageService = filesStorageService;
        this.fileRepository = fileRepository;
    }

    public FilesDto createFile(String fileName, MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidInputDataException("Файл пустой!");
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

    public void deleteFile(String filename) {
        FilesEntity entity = fileRepository.findByTitleAndNotDeleted(filename)
                                           .orElseThrow(
                                                   () -> new InvalidInputDataException(
                                                           "Нет файла с именем: " + filename));

        entity.setIsDeleted(true);
        try {
            fileRepository.save(entity);
        } catch (Exception e) {
            log.info("Не удалось записать в базу признак удалённого файла: {}", e.getMessage());

            throw new FilesException("Не удалось пометить файл как удалённый в базе. Причина: " + e.getMessage());
        }
    }

    public List<FilesDto> getAllWithLimit(Integer limit) {
        Pageable pageable = limit != null ? PageRequest.of(0, limit) : Pageable.unpaged();

        List<FilesEntity> filesEntities = fileRepository.findAllNotDeleted(pageable);

        return filesEntities.stream()
                            .map(FilesDto::new)
                            .toList();
    }

    public FilesEntity getFileEntityByTitle(String filename) {
        return fileRepository.findByTitleAndNotDeleted(filename)
                             .orElseThrow(() -> new InvalidInputDataException("Файл не найден: " + filename));
    }

    public Resource getFileResource(String path) {
        return filesStorageService.loadFileAsResource(path);
    }

    public void updateFileTitle(String currentFileName, String newFileName) {
        FilesEntity entity = fileRepository.findByTitleAndNotDeleted(currentFileName)
                                           .orElseThrow(() -> new InvalidInputDataException(
                                                   "Файл не найден: " + currentFileName));

        entity.setTitle(newFileName);
        log.debug("Обновляем имя объекта {}", entity);

        try {
            fileRepository.save(entity);
        } catch (Exception e) {
            log.info("Не удалось записать в базу новое имя файла: {}", e.getMessage());

            throw new FilesException("Новое имя файла не было сохранено. Причина: " + e.getMessage());
        }
    }
}
