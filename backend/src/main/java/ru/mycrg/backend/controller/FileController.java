package ru.mycrg.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mycrg.backend.dto.FilesDto;
import ru.mycrg.backend.dto.UpdateFileNameDto;
import ru.mycrg.backend.entity.FilesEntity;
import ru.mycrg.backend.service.FilesService;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/file")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    public final FilesService filesService;

    public FileController(FilesService filesService) {
        this.filesService = filesService;
    }

    @GetMapping
    public ResponseEntity<Resource> getFile(@RequestHeader("auth-token") String authToken,
                                            @RequestParam(value = "filename") String filename) {
        log.debug("authToken: {}, filename: {} ", authToken, filename);

        FilesEntity fileEntity = filesService.getFileEntityByName(filename);

        Resource resource = filesService.getFileResource(fileEntity.getPath());

        String hash = String.valueOf(fileEntity.getPath().hashCode());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        headers.add("X-File-Hash", hash);

        return ResponseEntity.ok()
                             .headers(headers)
                             .body(resource);
    }

    @PostMapping
    public ResponseEntity<FilesDto> postFile(@RequestHeader("auth-token") String authToken,
                                             @RequestParam(value = "filename", required = false) String filename,
                                             @RequestParam("file") MultipartFile file) {
        log.debug("authToken: {}", authToken);

        FilesDto filesDto = filesService.createFile(filename, file);

        return ResponseEntity.status(OK).body(filesDto);
    }

    @PutMapping
    public ResponseEntity<Object> putFile(@RequestHeader("auth-token") String authToken,
                                          @RequestParam(value = "filename") String filename,
                                          @RequestBody UpdateFileNameDto updateFileNameDto) {
        log.debug("authToken: {}, filename: {}, newName: {}", authToken, filename, updateFileNameDto.getName());

        filesService.updateFileName(filename, updateFileNameDto.getName());

        return ResponseEntity.status(OK).build();
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteFile(@RequestHeader("auth-token") String authToken,
                                             @RequestParam(value = "filename") String filename) {
        log.debug("authToken: {}, id: {}", authToken, filename);

        filesService.deleteFile(filename);

        return ResponseEntity.noContent().build();
    }
}
