package jm.demo.dao;

import org.springframework.stereotype.Repository;
import jm.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Role getRole(String name) {
        String query = "from Role where name='" + name +"'";
        Role role;
        try {
            role = (Role) entityManager.createQuery(query).getSingleResult();
        } catch (Exception e) {
            return null;
        }
        return role;
    }
}
