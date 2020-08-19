package jm.demo.controllers;

import jm.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DeleteController {

    UserService userService;

    @Autowired
    public DeleteController(UserService userService) {
        this.userService = userService;
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

}
