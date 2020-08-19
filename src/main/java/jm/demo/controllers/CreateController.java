package jm.demo.controllers;

import jm.demo.model.User;
import jm.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class CreateController {

    UserService userService;
    PasswordEncoder encoder;

    @Autowired
    public CreateController(UserService userService, PasswordEncoder encoder) {
        this.userService = userService;
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

}
