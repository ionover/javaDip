package ru.mycrg.backend.dto.response;

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

    private Integer size;

    private String filename;

    public FilesDto(FilesEntity entity) {
        this.filename = entity.getTitle();
        this.size = entity.getSize() != null ? entity.getSize().intValue() : 0;
    }
}
