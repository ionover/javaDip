package ru.mycrg.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FilesDto {

    @JsonProperty("filename")
    private String filename;
    
    private Integer size;

    public FilesDto() {
    }

    public FilesDto(FilesEntity entity) {
        this.filename = entity.getTitle();
        this.size = entity.getSize() != null ? entity.getSize().intValue() : 0;
    }
}
