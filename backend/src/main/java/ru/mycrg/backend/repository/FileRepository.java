package ru.mycrg.backend.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.mycrg.backend.entity.FilesEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<FilesEntity, UUID> {

    @Query("SELECT f FROM FilesEntity f WHERE f.isDeleted = false ORDER BY f.id DESC")
    List<FilesEntity> findAllNotDeleted(Pageable pageable);

    @Query("SELECT f FROM FilesEntity f WHERE f.title = :filename AND f.isDeleted = false")
    Optional<FilesEntity> findByTitleAndNotDeleted(String filename);
}
