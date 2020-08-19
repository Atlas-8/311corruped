package jm.demo.controllers;

import jm.demo.model.Role;
import jm.demo.model.User;
import jm.demo.service.RoleService;
import jm.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
public class EditController {

    UserService userService;
    RoleService roleService;
    PasswordEncoder encoder;

    @Autowired
    public EditController(UserService userService, RoleService roleService, PasswordEncoder encoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.encoder = encoder;
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


}
