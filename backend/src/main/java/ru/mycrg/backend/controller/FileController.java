package ru.mycrg.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mycrg.backend.FilesDto;
import ru.mycrg.backend.service.FilesService;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/file")
public class FileController {

    public final FilesService filesService;

    public FileController(FilesService filesService) {
        this.filesService = filesService;
    }

    @GetMapping
    public String getFile(@RequestParam UUID id) {

        return "helloworld";
    }

    @PostMapping
    public ResponseEntity<FilesDto> postFile(@RequestParam("file") MultipartFile file) {

        FilesDto filesDto = filesService.createFile(file);

        return ResponseEntity.status(CREATED).body(filesDto);
    }

    @PutMapping
    public String putFile() {
        return "Hello World";
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<FilesDto> deleteFile(@PathVariable String fileId) {

        FilesDto filesDto = filesService.deleteFile(fileId);

        return ResponseEntity.status(NO_CONTENT).body(filesDto);
    }
}
