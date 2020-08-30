package jm.demo.service;

import jm.demo.model.Role;
import jm.demo.model.User;
import jm.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final RoleService roleService;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(PasswordEncoder encoder, RoleService roleService, UserRepository userRepository) {
        this.roleService = roleService;
        this.encoder = encoder;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public void addUser(User user) {
        User userFromDB = userRepository.getUserByLogin(user.getUsername());
        if (userFromDB != null) {
            throw new RuntimeException("login is already exist");
        }
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getRole("ROLE_USER"));
        user.setRoles(roles);
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.saveAndFlush(user);
    }

    @Transactional
    @Override
    public void addAdmin(User user) {
        User userFromDB = userRepository.getUserByLogin(user.getUsername());
        if (userFromDB != null) {
            throw new RuntimeException("login is already exist");
        }
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getRole("ROLE_ADMIN"));
        user.setRoles(roles);
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.saveAndFlush(user);
    }

    @Override
    public void madeAdmin(User user){
        Role role = roleService.getRole("ROLE_ADMIN");
        role.getUsers().add(user);
        user.getRoles().add(role);
        userRepository.saveAndFlush(user);
    }

    @Override
    public void madeUser(User user){
        Role role = roleService.getRole("ROLE_USER");
        role.getUsers().add(user);
        user.getRoles().add(role);
        userRepository.saveAndFlush(user);
    }

    @Override
    public void dismissUser(User user){
        Role role = roleService.getRole("ROLE_USER");
        role.getUsers().remove(user);
        user.getRoles().remove(role);
        userRepository.saveAndFlush(user);
    }

    public void dismissAdmin(User user){
        Role role = roleService.getRole("ROLE_ADMIN");
        role.getUsers().remove(user);
        user.getRoles().remove(role);
        userRepository.saveAndFlush(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean deleteUser(long id) throws SQLException {
        userRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean updateUser(User user){
        userRepository.saveAndFlush(user);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.getUserByLogin(name);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    @Override
    public User getById(long id){
        return userRepository.getOne(id);
    }

    @Override
    public  User getByLogin(String login){
        return userRepository.getUserByLogin(login);
    }

}
