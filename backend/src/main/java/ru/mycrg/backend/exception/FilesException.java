package ru.mycrg.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class FilesException extends RuntimeException {


    //TODO: удалить?
    public FilesException(String message) {
        super(message);
    }
}
