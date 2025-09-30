package ru.mycrg.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mycrg.backend.dto.FilesDto;
import ru.mycrg.backend.service.FilesService;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/file")
public class FileController {

    public final FilesService filesService;

    public FileController(FilesService filesService) {
        this.filesService = filesService;
    }

    @GetMapping
    public String getFile(@RequestParam("id") UUID id) {

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteFile(@PathVariable("id") UUID id) {

        filesService.deleteFile(id);

        return ResponseEntity.noContent().build();
    }
}
