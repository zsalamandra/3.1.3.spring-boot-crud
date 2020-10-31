package web.springbootcrud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.springbootcrud.model.User;
import web.springbootcrud.service.UserService;

import java.util.List;

@RestController
public class ClientController {

    private final UserService service;

    @Autowired
    public ClientController(UserService service) {
        this.service = service;
    }

    @GetMapping(value = "/api/v1/current-user")
    public ResponseEntity<User> index(Authentication authentication) {
        User user = service.getUserByName(authentication.getName());
        return authentication != null ?
                new ResponseEntity<>(user, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/api/v1/users")
    public ResponseEntity<?> create(@RequestBody User user) {
        service.addUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/api/v1/users")
    public ResponseEntity<List<User>> readAll() {
        final List<User> users = service.listUsers();
        return users != null &&  !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/api/v1/users/{id}")
    public ResponseEntity<User> read(@PathVariable(name = "id") Long id) {
        User user = service.getUserById(id);
        return user != null
                ? new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/api/v1/users/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Long id, @RequestBody User user) {
        user.setId(id);
        service.addUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @DeleteMapping(value = "/api/v1/users/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        service.removeUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
