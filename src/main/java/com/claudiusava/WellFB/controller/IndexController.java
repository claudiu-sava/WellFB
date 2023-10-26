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


    @PostMapping("/new")
    private String newUser(@ModelAttribute User user){

        User userToDb = new User();
        userToDb.setUsername(user.getUsername());
        userToDb.setPassword(passwordEncoder().encode(user.getPassword()));

        Role roles = roleRepository.findByName("ROLE_USER").get();
        userToDb.setRoles(Collections.singleton(roles));

        userToDb.setPosts(null);

        userRepository.save(userToDb);

        return "redirect:/";
    }

}
