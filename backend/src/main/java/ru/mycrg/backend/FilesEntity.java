package ru.mycrg.backend;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "files", schema = "data")
public class FilesEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "size")
    private Long size;

    @Column(name = "extension", length = 10)
    private String extension;

    @Column(name = "path", nullable = false, length = 500)
    private String path;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructors
    public FilesEntity() {
    }

    public FilesEntity(String title, Long size, String extension, String path, String createdBy) {
        this.title = title;
        this.size = size;
        this.extension = extension;
        this.path = path;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
    }

    public FilesEntity(MultipartFile file, String path) {
        String originalFilename = file.getOriginalFilename();
        this.title = (originalFilename != null && !originalFilename.isEmpty()) ? originalFilename : "unknown_file";
        this.size = file.getSize();
        this.extension = extractExtension(originalFilename);
        this.path = path;
        this.createdBy = "system"; // можно оставить пустым или задать дефолтное значение
        this.createdAt = LocalDateTime.now();
    }

    private String extractExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return null;
        }
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex + 1);
        }
        return null;
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

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    @Override
    public String toString() {
        return "FilesEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", size=" + size +
                ", extension='" + extension + '\'' +
                ", path='" + path + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
