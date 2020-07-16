package jm.demo.dao;

import jm.demo.model.Role;
import jm.demo.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao{
   void add(User user);
   List<User> listUsers();
   void deleteUser(long id) throws SQLException;
   void updateUser(User user, long oldId);
   User findByUsername(String username);
   User getById(long id);
   void madeAdmin(User user, Role role);
   void dismissAdmin(User user, Role role);
}
