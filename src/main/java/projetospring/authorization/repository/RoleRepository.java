package projetospring.authorization.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import projetospring.authorization.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

}
