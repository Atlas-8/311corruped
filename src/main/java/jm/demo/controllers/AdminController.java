package jm.demo.controllers;

import jm.demo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import jm.demo.model.Role;
import jm.demo.model.User;
import jm.demo.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@Controller
@RequestMapping("/admin")
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
        List<User> users = userService.listUsers();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object obj = auth.getPrincipal();
        String username = "";
        if (obj instanceof UserDetails) {
            username = ((UserDetails) obj).getUsername();
        } else {
            username = obj.toString();
        }
        User admin = (User) userService.loadUserByUsername(username);
        model.addAttribute("users", users);
        model.addAttribute("admin", admin);
        return "users";
    }

    @GetMapping(value = "/saveUserPage")
    public String addition(ModelMap model, Authentication authentication) {
        model.addAttribute("user", new User());
        model.addAttribute("admin", authentication.getPrincipal());
        return "addition";
    }

    @PostMapping(value = "/saveUser")
    public void saveUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String adress = request.getParameter("adress");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("roles");

        User user = new User(name, adress, email, username, password);
        try {
            if (role.contains("Admin")){
                userService.madeAdmin(user);
            } else {
                userService.add(user);
            }
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        response.setContentType("text/html;charset=utf-8");
        response.sendRedirect("/admin/");
    }

    @GetMapping(value = "/getUserModal/{id}")
    public String getUserModal(ModelMap model, @PathVariable("id") long id){
        model.addAttribute("user", userService.getById(id));
        return "modalEdit";
    }

    @GetMapping(value = "/deleteUserModal/{id}")
    public String deleteUserModal(ModelMap model, @PathVariable("id") long id) throws SQLException {
        model.addAttribute("user", userService.getById(id));
        return "modalDelete";
    }

    @GetMapping(path = "/deleteUserAction/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, String> deleteUserAction(@PathVariable("id") long id) throws SQLException {
        HashMap<String, String> response = new HashMap<>();
        if (userService.deleteUser(id)) {
            response.put("status", "success");
            return response;
        }
        response.put("status", "error");
        return response;
    }

    @PostMapping(path = "/editUserAction/", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, String> editUserAction(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
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

}