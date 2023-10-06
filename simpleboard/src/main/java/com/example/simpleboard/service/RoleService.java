package com.example.simpleboard.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.simpleboard.entity.Role;
import com.example.simpleboard.repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository)
    {
        this.roleRepository = roleRepository;
    }

    public boolean createRole(Role role)
    {
        try {
            roleRepository.save(role);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public List<Role> getAllRoles()
    {
        return roleRepository.findAll();
    }

    public Role getRole(Long id)
    {
        try {
            return roleRepository.findById(id).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean updateRole(Role newRole)
    {
        try {
            Role old = roleRepository.findById(newRole.getId()).orElseThrow(()->{
                return new Exception(
                    "Role id[%d] not exist in repo".formatted(newRole.getId())
                )
                ;
            });
            old.updateRole(newRole);
            roleRepository.save(old);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean deleteRole(Long id)
    {
        try {
            roleRepository.deleteById(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}