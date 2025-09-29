package ru.mycrg.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mycrg.backend.FilesDto;
import ru.mycrg.backend.service.FilesService;

@RestController
@RequestMapping("/file")
public class FileController {

    public final FilesService filesService;

    public FileController(FilesService filesService) {
        this.filesService = filesService;
    }

    @GetMapping
    public String getFile() {
        return "Hello World";
    }

    @PostMapping
    public ResponseEntity<FilesDto> postFile(@RequestBody MultipartFile file) {

        FilesDto filesDto = filesService.createFile(file);

        return ResponseEntity.status(HttpStatus.CREATED).body(filesDto);
    }

    @PutMapping
    public String putFile() {
        return "Hello World";
    }

    @DeleteMapping
    public String deleteFile() {
        return "Hello World";
    }
}
