package ru.mycrg.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mycrg.backend.entity.UsersEntity;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UsersEntity, UUID> {

}
