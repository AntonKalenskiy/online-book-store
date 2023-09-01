package com.springframework.boot.onlinebookstore.service.impl;

import com.springframework.boot.onlinebookstore.model.Role;
import com.springframework.boot.onlinebookstore.repository.role.RoleRepository;
import com.springframework.boot.onlinebookstore.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role findByName(Role.RoleName roleName) {
        return roleRepository.findByName(roleName);
    }
}
