package jm.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jm.demo.dao.RoleDao;
import jm.demo.model.Role;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public Role getRole(String name){
        return roleDao.getRole(name);
    }
}
