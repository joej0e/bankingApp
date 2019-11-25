package spring.service;

import spring.entity.Role;

public interface RoleService {
    Role findByName(String name);
}
