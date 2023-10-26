package com.claudiusava.WellFB.controller;

import com.claudiusava.WellFB.model.Role;
import com.claudiusava.WellFB.model.User;
import com.claudiusava.WellFB.repository.RoleRepository;
import com.claudiusava.WellFB.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;

import static com.claudiusava.WellFB.security.SecurityConfiguration.passwordEncoder;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/new")
    private String ShowNew(){
        return "signup";
    }


    @PostMapping("/new")
    private String newUser(@ModelAttribute User user){

        User userToDb = new User();
        userToDb.setUsername(user.getUsername());
        userToDb.setPassword(passwordEncoder().encode(user.getPassword()));

        Role roles = roleRepository.findByName("ROLE_USER").get();
        userToDb.setRoles(Collections.singleton(roles));

        userToDb.setPosts(null);

        userRepository.save(userToDb);

        return "redirect:/login";
    }

}
