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
    public void deleteUser(long id) throws SQLException {
        userRepository.deleteById(id);
    }

    @Override
    public void updateUser(String name, String adress, String email, String login, String password, long oldId){
        User userDto = userRepository.findById(oldId).get();
        userDto.setName(name);
        userDto.setName(email);
        userDto.setName(adress);
        userDto.setName(login);
        userDto.setPassword(encoder.encode(password));
        userRepository.saveAndFlush(userDto);
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
}
