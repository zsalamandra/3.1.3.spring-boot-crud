package web.springbootcrud.service;

import web.springbootcrud.model.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    void addUser(User user, String[] roles);
    void removeUser(Long id);
    User getUserById(Long id);
    List<User> listUsers();
    Map<String, Boolean> listRoles();
    Map<String, Boolean> getRolesByUserId(Long id);
    User getUserByName(String username);
}
