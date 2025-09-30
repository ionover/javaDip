package ru.mycrg.backend;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FilesDto {

    private String fileName;
    private Long size;

    public FilesDto() {
    }

    public FilesDto(FilesEntity entity) {
        this.fileName = entity.getTitle();
        this.size = entity.getSize();
    }
}
