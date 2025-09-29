package ru.mycrg.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.mycrg.backend.FilesEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<FilesEntity, UUID> {

    /**
     * Find files by title containing the given string (case insensitive)
     */
    List<FilesEntity> findByTitleContainingIgnoreCase(String title);

    /**
     * Find files by extension
     */
    List<FilesEntity> findByExtension(String extension);

    /**
     * Find files by creator
     */
    List<FilesEntity> findByCreatedBy(String createdBy);

    /**
     * Find files created after a specific date
     */
    List<FilesEntity> findByCreatedAtAfter(LocalDateTime date);

    /**
     * Find files created between two dates
     */
    List<FilesEntity> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find files by size greater than specified value
     */
    List<FilesEntity> findBySizeGreaterThan(Long size);

    /**
     * Find files by size less than specified value
     */
    List<FilesEntity> findBySizeLessThan(Long size);

    /**
     * Find files by path containing the given string
     */
    List<FilesEntity> findByPathContaining(String pathPart);

    /**
     * Find files by exact path
     */
    Optional<FilesEntity> findByPath(String path);

    /**
     * Check if file exists by path
     */
    boolean existsByPath(String path);

    /**
     * Count files by extension
     */
    long countByExtension(String extension);

    /**
     * Count files by creator
     */
    long countByCreatedBy(String createdBy);

    /**
     * Get total size of all files
     */
    @Query("SELECT SUM(f.size) FROM FilesEntity f")
    Long getTotalFilesSize();

    /**
     * Get total size of files by creator
     */
    @Query("SELECT SUM(f.size) FROM FilesEntity f WHERE f.createdBy = :createdBy")
    Long getTotalFilesSizeByCreator(@Param("createdBy") String createdBy);

    /**
     * Find files with pagination and sorting by creation date
     */
    Page<FilesEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

    /**
     * Find files by creator with pagination
     */
    Page<FilesEntity> findByCreatedBy(String createdBy, Pageable pageable);

    /**
     * Find files by extension with pagination
     */
    Page<FilesEntity> findByExtension(String extension, Pageable pageable);

    /**
     * Search files by title with pagination
     */
    Page<FilesEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    /**
     * Delete files by creator
     */
    void deleteByCreatedBy(String createdBy);

    /**
     * Delete files older than specified date
     */
    void deleteByCreatedAtBefore(LocalDateTime date);
}
