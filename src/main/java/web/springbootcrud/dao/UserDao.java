package web.springbootcrud.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import web.springbootcrud.model.User;

public interface UserDao extends JpaRepository<User, Long> {
    User findByName(String name);
}
