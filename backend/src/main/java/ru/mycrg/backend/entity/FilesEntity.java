package ru.mycrg.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.multipart.MultipartFile;
import ru.mycrg.backend.util.FileUtils;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "files", schema = "data")
public class FilesEntity {

    // Getters and Setters
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

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

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

    public FilesEntity(String title, MultipartFile file, String path) {
        String originalFilename = file.getOriginalFilename();
        this.title = title;
        this.size = file.getSize();
        this.extension = FileUtils.extractExtension(originalFilename);
        this.path = path;
        this.createdBy = "system"; // можно оставить пустым или задать дефолтное значение
        this.createdAt = LocalDateTime.now();
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
                ", isDeleted=" + isDeleted +
                '}';
    }
}
