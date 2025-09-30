package ru.mycrg.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mycrg.backend.dto.FilesDto;
import ru.mycrg.backend.service.FilesService;

import java.util.List;

@RestController
public class FilesListController {

    private final FilesService filesService;

    public FilesListController(FilesService filesService) {
        this.filesService = filesService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<FilesDto>> getFiles(@RequestParam(required = false) Integer limit) {

        List<FilesDto> filesDtoList = filesService.getAllWithLimit(limit);

        return ResponseEntity.ok(filesDtoList);
    }
}
