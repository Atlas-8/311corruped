package jm.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import jm.demo.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> listUsers();
    void addUser(User user);
    boolean deleteUser(long id) throws SQLException;
    boolean updateUser(User user);
    UserDetails loadUserByUsername(String var1) throws UsernameNotFoundException;
    User getById(long id);
    void madeAdmin(User user);
    void dismissAdmin(User user);
    void dismissUser(User user);
    void madeUser(User user);
    void addAdmin(User user);

}
