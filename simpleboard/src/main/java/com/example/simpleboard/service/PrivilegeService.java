package com.example.simpleboard.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.simpleboard.entity.Privilege;
import com.example.simpleboard.repository.PrivilegeRepository;

@Service
public class PrivilegeService {
    
    private final PrivilegeRepository privilegeRepository;

    public PrivilegeService(PrivilegeRepository privilegeRepository)
    {
        this.privilegeRepository = privilegeRepository;
    }

    public Page<Privilege> getAllPrivileges(Pageable pageable)
    {
        try {
            return privilegeRepository.findAll(pageable);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean createPrivilege(Privilege privilege)
    {
        try {
            privilegeRepository.save(privilege);
        } catch (Exception e) {
             return false;
       }
       return true;
    }

    public Privilege getPrivilege(long id)
    {
        try {
            return privilegeRepository.findById(id).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public Privilege getPrivilegeByName(String name)
    {
        try {
            return privilegeRepository.findByName(name).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean updatePrivilege(Privilege other)
    {
        try {
            var origin = privilegeRepository.findById(other.getId()).orElse(null);
            if(origin == null)
            {
                return false;
            }
            origin.updatePrivilege(other);
            privilegeRepository.save(origin);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean deletePrivilege(long id)
    {
        try {
            privilegeRepository.deleteById(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
