package ru.mycrg.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mycrg.backend.entity.UsersEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UsersEntity, UUID> {
    
    Optional<UsersEntity> findByLoginAndPassword(String login, String password);
    
    Optional<UsersEntity> findByJwtToken(String jwtToken);
}
