package web.springbootcrud.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import web.springbootcrud.model.Role;

public interface RoleDao extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
