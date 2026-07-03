package projetospring.authorization.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import projetospring.authorization.entity.User;

public interface UserRepository extends JpaRepository<User, UUID>{

}
