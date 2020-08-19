package jm.demo.service;

import jm.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jm.demo.model.Role;
import jm.demo.model.User;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

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
    public void add(User user) {
        User userFromDB = userRepository.getUserByLogin(user.getUsername());
        if (userFromDB != null) {
            throw new RuntimeException("login is already exist");
        }
        user.setRoles(Collections.singleton(roleService.getRole("ROLE_USER")));
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
    public boolean updateUser(String name, String adress, String email, String login, String password, long oldId){
        User userFromDB = userRepository.getUserByLogin(login);
        User userDto = userRepository.findById(oldId).get();
        userDto.setUsername(login);
        userDto.setPassword(encoder.encode(password));
        userDto.setName(name);
        userDto.setEmail(email);
        userDto.setAdress(adress);
        userRepository.saveAndFlush(userDto);
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

    @Override
    public void newAdmin(User user){
        User userFromDB = userRepository.getUserByLogin(user.getUsername());
        if (userFromDB != null) {
            throw new RuntimeException("login is already exist");
        }
        user.setRoles(Collections.singleton(roleService.getRole("ROLE_ADMIN")));
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.saveAndFlush(user);
    }
}
