package ru.mycrg.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mycrg.backend.entity.FilesEntity;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FilesDto {

    @JsonProperty("filename")
    private String filename;

    private Integer size;

    public FilesDto(FilesEntity entity) {
        this.filename = entity.getTitle();
        this.size = entity.getSize() != null ? entity.getSize().intValue() : 0;
    }
}
