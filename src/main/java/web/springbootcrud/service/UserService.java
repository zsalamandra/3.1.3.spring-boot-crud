package web.springbootcrud.service;

import web.springbootcrud.model.Role;
import web.springbootcrud.model.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    void addUser(User user);
    void removeUser(Long id);
    User getUserById(Long id);
    List<User> listUsers();
    List<Role> listRoles();
    Map<String, Boolean> getRolesByUserId(Long id);
    User getUserByName(String username);
}
