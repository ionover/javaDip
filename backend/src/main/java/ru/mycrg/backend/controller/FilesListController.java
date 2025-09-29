package ru.mycrg.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FilesListController {

    @GetMapping("/list")
    public List<File> getFiles() {
        return new ArrayList<>();
    }
}
