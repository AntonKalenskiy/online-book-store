package com.springframework.boot.onlinebookstore.service;

import com.springframework.boot.onlinebookstore.model.Role;

public interface RoleService {
    Role findByName(Role.RoleName roleName);
}
