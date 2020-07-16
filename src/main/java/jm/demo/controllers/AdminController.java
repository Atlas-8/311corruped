package jm.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jm.demo.model.Role;
import jm.demo.model.User;
import jm.demo.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
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

    @GetMapping(value = "/madeAdmin/{id}")
    public void madeAdmin(@PathVariable("id") long id, HttpServletResponse response) throws IOException {
        User user = userService.getById(id);
        Set<Role> roles = user.getRoles();
        if (!roles.contains(new Role(1L, "ROLE_ADMIN"))){
            userService.madeAdmin(user);
        }
        response.sendRedirect("/admin/");
    }

    @GetMapping(value = "/dismissAdmin/{id}")
    public void dismissAdmin(@PathVariable("id") long id, HttpServletResponse response) throws IOException {
        User user = userService.getById(id);
        Set<Role> roles = user.getRoles();
        if (roles.contains(new Role(1L, "ROLE_ADMIN"))){
            userService.dismissAdmin(user);
        }
        response.sendRedirect("/admin/");
    }

    @GetMapping(value = "/saveUser")
    public String addition(ModelMap model) {
        model.addAttribute("user", new User());
        return "addition";
    }

    @PostMapping(value = "/saveUser")
    public void saveUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String adress = request.getParameter("adress");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = new User(name, adress, email, username, password);
        try {
            userService.add(user);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
        response.setContentType("text/html;charset=utf-8");
        response.sendRedirect("/admin/");
    }

    @GetMapping(value = "/deleteUser/{id}")
    public void deleteUser(HttpServletResponse response, @PathVariable("id") long id) throws IOException, SQLException {
        userService.deleteUser(id);
        response.setContentType("text/html;charset=utf-8");
        response.sendRedirect("/admin/");
    }

    @GetMapping(value = "/updateUser/{id}")
    public String updation(ModelMap model, @PathVariable("id") long id){
        model.addAttribute("user", userService.getById(id));
        return "updation";
    }

    @PostMapping(value = "/updateUser/{oldId}")
    public void updateUser(HttpServletRequest request, HttpServletResponse response, @PathVariable("oldId") long oldId) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");
        String adress = request.getParameter("adress");
        String email = request.getParameter("email");
        String login = request.getParameter("username");
        String password = request.getParameter("password");
        userService.updateUser(name, adress, email, login, password, oldId);
        response.sendRedirect("/admin/");
    }
}