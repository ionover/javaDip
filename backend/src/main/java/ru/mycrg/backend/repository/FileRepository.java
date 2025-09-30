package ru.mycrg.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.mycrg.backend.FilesEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<FilesEntity, UUID> {

    @Query("SELECT * FROM FilesEntity f WHERE f.isDeleted=false LIMIT :limit")
    List<FilesEntity> getAllWithLimit(@Param("limit") Integer limit);
}
