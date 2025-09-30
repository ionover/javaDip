package ru.mycrg.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mycrg.backend.dto.FilesDto;
import ru.mycrg.backend.service.FilesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
public class FilesListController {

    private static final Logger logger = LoggerFactory.getLogger(FilesListController.class);
    private final FilesService filesService;

    public FilesListController(FilesService filesService) {
        this.filesService = filesService;
    }

    @GetMapping("/list")
    public ResponseEntity<FilesDto> getFiles(@RequestHeader("auth-token") String authToken,
                                             @RequestParam(value = "limit", required = false) Integer limit) {

        logger.info("FilesListController: GET /list called with limit={}", limit);
        
        List<FilesDto> filesDtoList = filesService.getAllWithLimit(limit);
        logger.info("Retrieved {} files from service", filesDtoList.size());

        // Согласно спецификации возвращаем один объект, а не массив
        // Берем первый файл из списка или создаем пустой объект если список пуст
        FilesDto response = filesDtoList.isEmpty() ? new FilesDto() : filesDtoList.get(0);
        logger.info("Returning response: filename={}, size={}", response.getFilename(), response.getSize());

        return ResponseEntity.ok(response);
    }
}
