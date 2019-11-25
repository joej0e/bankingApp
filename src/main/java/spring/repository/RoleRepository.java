package spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
