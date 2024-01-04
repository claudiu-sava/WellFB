package com.claudiusava.WellFB.service;

import com.claudiusava.WellFB.model.User;
import com.claudiusava.WellFB.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SessionService {
    @Autowired
    private UserRepository userRepository;
    public String getLoggedUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public User getLoggedUser(){
        return userRepository.findByUsername(getLoggedUsername()).get();
    }

    public boolean isLoggedUserAdmin(){

        User user = getLoggedUser();
        if (user.getRoles().iterator().next().getName().equals("ROLE_ADMIN")){

            return true;

        }

        return false;

    }

}
