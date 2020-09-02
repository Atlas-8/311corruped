package jm.demo.controllers;

import jm.demo.model.User;
import jm.demo.service.RoleService;
import jm.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ActionController {

    UserService userService;
    RoleService roleService;
    PasswordEncoder encoder;

    @Autowired
    public ActionController(UserService userService, RoleService roleService, PasswordEncoder encoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.encoder = encoder;
    }

    @PostMapping(value = "/saveUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<User> saveUser(@RequestBody User user) {
        try {

            if (user.getRoles().size() > 1){
                userService.addUser(user);
                userService.madeAdmin(user);
            } else if (user.getRoles().contains(roleService.getRole("ROLE_ADMIN"))) {
                userService.addAdmin(user);
            } else {
                userService.addUser(user);
            }

        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(path = "/editUserAction/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> editUserAction(@RequestBody User user) {

        User userFromDB = userService.getById(user.getId());

        if (user.getRoles().size() > 1){
            userService.madeUser(user);
            userService.madeAdmin(user);
        } else if (user.getRoles().contains(roleService.getRole("ROLE_ADMIN"))) {
            userService.dismissUser(user);
            userService.madeAdmin(user);
        } else if (user.getRoles().contains(roleService.getRole("ROLE_USER"))) {
            userService.dismissAdmin(user);
            userService.madeUser(user);
        } else {
            user.setRoles(userFromDB.getRoles());
        }
        userService.updateUser(user);

        return new ResponseEntity<>(user, HttpStatus.OK);

    }

    @GetMapping(path = "/deleteUserAction/{id}")
    public Map<String, String> deleteUserAction(@PathVariable("id") long id) throws SQLException {
        HashMap<String, String> response = new HashMap<>();
        if (userService.deleteUser(id)) {
            response.put("status", "success");
            return response;
        }
        response.put("status", "error");
        return response;
    }

    @GetMapping(value = "/getUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userService.listUsers(), HttpStatus.OK);
    }

}
