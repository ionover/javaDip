package ru.mycrg.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mycrg.backend.dto.response.FilesDto;
import ru.mycrg.backend.exception.InvalidInputDataException;
import ru.mycrg.backend.service.FilesService;

import java.util.List;

@RestController
public class FilesListController {

    private static final Logger log = LoggerFactory.getLogger(FilesListController.class);
    private final FilesService filesService;

    public FilesListController(FilesService filesService) {
        this.filesService = filesService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<FilesDto>> getFiles(@RequestHeader("auth-token") String authToken,
                                                   @RequestParam(value = "limit", required = false) Integer limit) {

        log.debug("auth-token: {}", authToken);

        if (limit != null && limit < 0) {
            throw new InvalidInputDataException("Error input data", 0);
        }

        List<FilesDto> filesDtoList = filesService.getAllWithLimit(limit);
        log.info("Количество найденных файлов: {}", filesDtoList.size());

        return ResponseEntity.ok(filesDtoList);
    }
}
