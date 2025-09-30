package ru.mycrg.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mycrg.backend.dto.FilesDto;
import ru.mycrg.backend.exception.AuthException;
import ru.mycrg.backend.service.FilesService;
import ru.mycrg.backend.service.JwtService;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/file")
public class FileController {

    public final FilesService filesService;
    private final JwtService jwtService;

    public FileController(FilesService filesService, JwtService jwtService) {
        this.filesService = filesService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public String getFile(@RequestParam("id") UUID id) {

        return "helloworld";
    }

    @PostMapping
    public ResponseEntity<FilesDto> postFile(@RequestHeader("auth-token") String authToken,
                                             @RequestParam(value = "filename", required = false) String filename,
                                             @RequestParam("file") MultipartFile file) {

        if (jwtService.validateToken(authToken)) {
            throw new AuthException("Невалидный токен");
        }

        FilesDto filesDto = filesService.createFile(filename, file);

        return ResponseEntity.status(CREATED).body(filesDto);
    }

    @PutMapping
    public String putFile() {
        return "Hello World";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteFile(@PathVariable("id") UUID id) {

        filesService.deleteFile(id);

        return ResponseEntity.noContent().build();
    }
}
