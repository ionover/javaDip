package ru.mycrg.backend.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"stackTrace", "cause", "suppressed", "localizedMessage"})
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidInputDataException extends RuntimeException {

    private String message;
    private int id;

    public InvalidInputDataException(String message) {
        this.message = message;
    }
}
