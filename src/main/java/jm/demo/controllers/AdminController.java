package jm.demo.controllers;

import jm.demo.model.User;
import jm.demo.service.RoleService;
import jm.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class AdminController {

    UserService userService;
    RoleService roleService;
    PasswordEncoder encoder;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, PasswordEncoder encoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.encoder = encoder;
    }

    @GetMapping
    public String printUsers(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object obj = auth.getPrincipal();
        String username = "";
        if (obj instanceof UserDetails) {
            username = ((UserDetails) obj).getUsername();
        } else {
            username = obj.toString();
        }
        User admin = (User) userService.loadUserByUsername(username);
        model.addAttribute("admin", admin);
        return "users";
    }

    @GetMapping(value = "/getUserModal/{id}")
    public String getUserModal(ModelMap model, @PathVariable("id") long id){
        model.addAttribute("user", userService.getById(id));
        return "modalEdit";
    }

    @GetMapping(value = "/deleteUserModal/{id}")
    public String deleteUserModal(ModelMap model, @PathVariable("id") long id) {
        model.addAttribute("user", userService.getById(id));
        return "modalDelete";
    }

}