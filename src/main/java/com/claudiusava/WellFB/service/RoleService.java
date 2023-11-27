package com.claudiusava.WellFB.service;

import com.claudiusava.WellFB.model.Role;
import com.claudiusava.WellFB.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;


    public Role getRoleByName(String name){

        Optional<Role> roleOptional = roleRepository.findByName(name);

        if(roleOptional.isPresent()){
            return roleOptional.get();
        }

        return null;

    }
}
