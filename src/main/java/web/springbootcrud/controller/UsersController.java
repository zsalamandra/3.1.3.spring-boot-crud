package web.springbootcrud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import web.springbootcrud.model.User;
import web.springbootcrud.service.UserService;

import java.security.Principal;

@Controller
public class UsersController {

    @Autowired
    private UserService userService;

    @GetMapping("/auth/login")
    public String getLoginPage() {
        return "login";
    }

//    ************ USERS LIST ***********************
    @GetMapping(value = "/adm/users")
    public String index(Model model, Authentication authentication) {
        model.addAttribute("authentication", authentication);
//        User user = userService.getUserByName(authentication.getName());
//        model.addAttribute("currentuser", user);
//        передается новоиспеченный пользователь, данный пользователь будет получен
//                в методе POST для сохранения
//        model.addAttribute("newuser", new User());
//        передаем всех юзеров для отображения в таблице
//        model.addAttribute("users", userService.listUsers());
        return "users";
    }



//    ************** NEW USER **************
    @GetMapping("/adm/users/newuser")
    public User newPerson() {
        return new User();
    }

    @PostMapping("/adm/users/newuser")
    public String create(@ModelAttribute("newuser") User user) {
        //userService.addUser(user);
        return "redirect:/adm/users";
    }



//    ********************** EDIT ******************************
    @GetMapping("/adm/users/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("operationType", "edit");
        return "new";
    }
    @PostMapping("/adm/users/{id}/edit")
    public String update(@ModelAttribute("user") User user) {
        //userService.addUser(user);
        return "redirect:/adm/users";
    }


//    ***************** DELETE **********************************
    @GetMapping("/adm/users/{id}/delete")
    public String deleteUser(Model model, @PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("operationType", "delete");
        return "new";
    }

    @PostMapping("/adm/users/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        userService.removeUser(id);
        return "redirect:/adm/users";
    }



    @GetMapping("/UserProfile/{id}")
    public String show(Model model, @PathVariable("id") Long id, Authentication authentication) {
        model.addAttribute("authentication", authentication);
        return "users";
    }
}
