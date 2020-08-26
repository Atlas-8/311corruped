package jm.demo.controllers;

import jm.demo.model.Role;
import jm.demo.model.User;
import jm.demo.service.RoleService;
import jm.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @PostMapping(value = "/saveUser")
    public void saveUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String adress = request.getParameter("adress");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        User user = new User(name, adress, email, username, password);
        try {
            if (role.contains("Admin")){
                userService.newAdmin(user);
            } else {
                userService.add(user);
            }
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        response.setContentType("text/html;charset=utf-8");
        response.sendRedirect("/users/");
    }

    @PostMapping(path = "/editUserAction/")
    public Map<String, String> editUserAction(HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("id");
        long id = Long.parseLong(idStr);
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String adress = request.getParameter("adress");
        String role = request.getParameter("role");

        User user = userService.getById(id);
        Set<Role> roles = user.getRoles();

        if (role.equals("Admin")){
            userService.madeAdmin(user);
            userService.dismissUser(user);
        }
        if (role.equals("User")){
            userService.madeUser(user);
            userService.dismissAdmin(user);
        }
        if (role.equals("Admin,User")){
            if (roles.contains(roleService.getRole("ROLE_USER"))){
                userService.madeAdmin(user);
            } else if (roles.contains(roleService.getRole("ROLE_ADMIN"))){
                userService.madeUser(user);
            }
        }

        HashMap<String, String> resp = new HashMap<>();
        if (userService.updateUser(name, adress, email, login, password, id)) {
            resp.put("status", "success");
            resp.put("message", "added to update user values: " + "name" + name + ", " + "adress" + adress + ", " + "email" + email + ", " + "login" + login + ", " + "password" + password + ", " + "id" + id);
            return resp;
        }
        resp.put("status", "error");
        return resp;
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

    @GetMapping(value = "/getUsers")
    public @ResponseBody List<User> getUsers() {
        return userService.listUsers();
    }

    @GetMapping(value = "/getUser/{id}")
    public @ResponseBody User getById(@PathVariable ("id") long id) {
        return userService.getById(id);
    }

}
