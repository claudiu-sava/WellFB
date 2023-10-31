package com.claudiusava.WellFB.controller;

import com.claudiusava.WellFB.model.Post;
import com.claudiusava.WellFB.model.Role;
import com.claudiusava.WellFB.model.User;
import com.claudiusava.WellFB.repository.PostRepository;
import com.claudiusava.WellFB.repository.RoleRepository;
import com.claudiusava.WellFB.repository.UserRepository;
import com.claudiusava.WellFB.service.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;

import static com.claudiusava.WellFB.WellFbApplication.UPLOAD_DIRECTORY;
import static com.claudiusava.WellFB.security.SecurityConfiguration.passwordEncoder;

@Controller
public class IndexController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;


    @GetMapping("/")
    private String showIndexPage(Model model){

        User loggedUser = userRepository.findByUsername(User.getLoggedUsername()).get();

        Iterable<Post> allPosts = postRepository.findAll();
        Iterable<User> allUsers = userRepository.findFirst10By();

        Iterator<User> iterator = allUsers.iterator();
        while (iterator.hasNext()){
            if(iterator.next().equals(loggedUser)){
                iterator.remove();
            }
        }

        model.addAttribute("title", "WellFB");
        model.addAttribute("loggedUser", loggedUser);

        model.addAttribute("allPosts", allPosts);
        model.addAttribute("allUsers", allUsers);

        return "index";
    }

}
