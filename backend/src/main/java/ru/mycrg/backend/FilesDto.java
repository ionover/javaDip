package ru.mycrg.backend;

import java.time.LocalDateTime;
import java.util.UUID;

public class FilesDto {

    private UUID id;
    private String title;
    private Long size;
    private String extension;
    private String path;
    private String createdBy;
    private LocalDateTime createdAt;

    public FilesDto() {
    }

    public FilesDto(FilesEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.size = entity.getSize();
        this.extension = entity.getExtension();
        this.path = entity.getPath();
        this.createdBy = entity.getCreatedBy();
        this.createdAt = entity.getCreatedAt();
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
