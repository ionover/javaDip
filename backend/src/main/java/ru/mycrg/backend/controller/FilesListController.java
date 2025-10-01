package ru.mycrg.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mycrg.backend.dto.FilesDto;
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

        List<FilesDto> filesDtoList = filesService.getAllWithLimit(limit);
        log.info("Retrieved {} files from service", filesDtoList.size());

        return ResponseEntity.ok(filesDtoList);
    }
}
