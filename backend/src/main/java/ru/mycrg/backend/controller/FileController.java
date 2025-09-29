package ru.mycrg.backend.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/file")
public class FileController {

    @GetMapping
    public String getFile() {
        return "Hello World";
    }

    @PostMapping
    public String postFile() {
        return "Hello World";
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
