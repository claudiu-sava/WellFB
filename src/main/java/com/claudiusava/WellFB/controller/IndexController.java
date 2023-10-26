package com.claudiusava.WellFB.controller;

import com.claudiusava.WellFB.model.Post;
import com.claudiusava.WellFB.model.Role;
import com.claudiusava.WellFB.model.User;
import com.claudiusava.WellFB.repository.PostRepository;
import com.claudiusava.WellFB.repository.RoleRepository;
import com.claudiusava.WellFB.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

import static com.claudiusava.WellFB.security.SecurityConfiguration.passwordEncoder;

@Controller
public class IndexController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/")
    private String showIndexPage(Model model){

        Iterable<Post> allPosts = postRepository.findAll();
        Iterable<User> allUsers = userRepository.findFirst10By();

        model.addAttribute("allPosts", allPosts);
        model.addAttribute("allUsers", allUsers);

        model.addAttribute("title", "WellFB");

        return "index";
    }

    @GetMapping("/admin")
    private String test2(){
        return "ADMIN";
    }

    @GetMapping("/new")
    private String ShowNew(){
        return "signup";
    }

    @PostMapping("/new")
    private String newUser(@ModelAttribute User user){

        User user1 = new User();
        user1.setUsername(user.getUsername());
        user1.setPassword(passwordEncoder().encode(user.getPassword()));

        Role roles = roleRepository.findByName("ROLE_ADMIN").get();
        user1.setRoles(Collections.singleton(roles));

        user1.setPosts(null);

        userRepository.save(user1);

        System.out.println("USER SAVED");


        return "redirect:/";
    }

}
