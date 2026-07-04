package projetospring.authorization.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projetospring.authorization.entity.Role;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
    Role findByNome(String nome);
}
