package ru.mycrg.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mycrg.backend.dto.FilesDto;
import ru.mycrg.backend.exception.AuthException;
import ru.mycrg.backend.service.FilesService;
import ru.mycrg.backend.service.JwtService;

import java.util.List;

@RestController
public class FilesListController {

    private final FilesService filesService;
    private final JwtService jwtService;

    public FilesListController(FilesService filesService, JwtService jwtService) {
        this.filesService = filesService;
        this.jwtService = jwtService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<FilesDto>> getFiles(@RequestHeader("auth-token") String authToken,
                                                   @RequestParam(required = false) Integer limit) {

        if (!jwtService.isTokenValid(authToken)) {
            throw new AuthException("Невалидный токен");
        }

        List<FilesDto> filesDtoList = filesService.getAllWithLimit(limit);

        return ResponseEntity.ok(filesDtoList);
    }
}
