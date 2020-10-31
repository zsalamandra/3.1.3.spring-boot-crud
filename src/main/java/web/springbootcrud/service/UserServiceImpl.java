package web.springbootcrud.service;

import org.springframework.stereotype.Service;
import web.springbootcrud.dao.RoleDao;
import web.springbootcrud.dao.UserDao;
import web.springbootcrud.model.Role;
import web.springbootcrud.model.User;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final RoleDao roleDao;

    public UserServiceImpl(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    private void createCyclicReference(User user){
        Set<Role> userRoles = new HashSet<>();
        for (Role role: user.getRoles()){
            Long id = roleDao.findByName(role.getName()).getId();
            userRoles.add(new Role(id, role.getName(), user));
        }
        user.setRoles(userRoles);
    }

    @Override
    public void addUser(User user) {
        createCyclicReference(user);
        userDao.save(user);
    }

    @Override
    public void removeUser(Long id) {
        userDao.deleteById(id);
    }

    @Override
    public User getUserById(Long id) {
        User user = userDao.getOne(id);
        User retUser = new User(
                user.getId(),
                user.getName(),
                user.getPassword(),
                user.getRoles()
        );
        return retUser;
    }

    @Override
    public User getUserByName(String name) {
        return userDao.findByName(name);
    }

    @Override
    public List<User> listUsers() {
        return userDao.findAll();
    }

    @Override
    public Map<String, Boolean> listRoles() {
        Map<String, Boolean> roleBooleanMap = new HashMap<>();
        Boolean userHasRole = true;
        for (Role role: roleDao.findAll()){
            roleBooleanMap.put(role.getName(), userHasRole);
            userHasRole = false;
        }
        return roleBooleanMap;
    }

    @Override
    public Map<String, Boolean> getRolesByUserId(Long id) {
        Map<String, Boolean> roleBooleanMap = new HashMap<>();

        for (Role role: roleDao.findAll()){
            Boolean userHasRole = getUserById(id)
                    .getRoles()
                    .toString()
                    .contains(role.getName());
            roleBooleanMap.put(role.getName(), userHasRole);
        }
        return roleBooleanMap;
    }
}