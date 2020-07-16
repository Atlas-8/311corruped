package jm.demo.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import jm.demo.model.Role;
import jm.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

   @PersistenceContext
   EntityManager entityManager;

   @Override
   public void add(User user) {
      entityManager.persist(user);
   }

   @Override
   public List<User> listUsers() {
      return entityManager.createQuery("from User").getResultList();
   }

   @Override
   @Transactional
   public void madeAdmin(User user, Role role){
      user.getRoles().add(role);
      role.getUsers().add(user);
      entityManager.merge(user);
      entityManager.flush();
   }

   @Override
   @Transactional
   public void dismissAdmin(User user, Role role){
      user.getRoles().remove(role);
      role.getUsers().remove(user);
      entityManager.merge(user);
      entityManager.flush();
   }

   @Override
   @Transactional
   public void deleteUser(long id) throws SQLException {
      String sql = "delete from User where id=" + id;
      entityManager.createQuery(sql).executeUpdate();
   }

   @Override
   @Transactional
   public void updateUser(User user, long oldId){
      entityManager.createQuery("UPDATE User SET name =:name, adress =:adress, email =:email, login =:login, password=:password where id = :oldId")
              .setParameter("name", user.getName())
              .setParameter("adress", user.getAdress())
              .setParameter("email", user.getEmail())
              .setParameter("login", user.getUsername())
              .setParameter("password", user.getPassword())
              .setParameter("oldId", oldId)
              .executeUpdate();
   }

   @Override
   public User findByUsername(String username) {
      String query = "from User where login='" + username +"'";
      User user;
      try {
         user = (User) entityManager.createQuery(query).getSingleResult();
      } catch (Exception e) {
         return null;
      }
      return user;
   }

   @Override
   public User getById(long id){
      String query = "from User where id=" + id;
      User user;
      try {
         user = (User) entityManager.createQuery(query).getSingleResult();
      } catch (Exception e) {
         return null;
      }
      return user;
   }

}
