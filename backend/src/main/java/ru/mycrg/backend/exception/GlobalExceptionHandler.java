package ru.mycrg.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FilesException.class)
    public ResponseEntity<FilesException> handleFilesException(FilesException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<AuthException> handleAuthException(AuthException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<InvalidInputDataException> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        InvalidInputDataException error = new InvalidInputDataException("Error input data", 0);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
