package web.springbootcrud.service;

import web.springbootcrud.model.Role;
import web.springbootcrud.model.User;

import java.util.List;

public interface UserService {
    void addUser(User user);
    void removeUser(Long id);
    User getUserById(Long id);
    List<User> listUsers();
    User getUserByName(String username);
}
