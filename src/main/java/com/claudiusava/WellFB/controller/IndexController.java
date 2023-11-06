package com.claudiusava.WellFB.controller;

import com.claudiusava.WellFB.model.Post;
import com.claudiusava.WellFB.model.User;
import com.claudiusava.WellFB.repository.PostRepository;
import com.claudiusava.WellFB.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;


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
        List<User> list10Users = new ArrayList<>();

        List<User> allUsers = userRepository.findAllByOrderByUsernameAsc();
        allUsers.remove(loggedUser);
        Collections.shuffle(allUsers);

        for (int i = 0; i <= 9; i ++){
            list10Users.add(allUsers.get(i));
        }

        model.addAttribute("title", "WellFB");
        model.addAttribute("loggedUser", loggedUser);

        model.addAttribute("allPosts", allPosts);
        model.addAttribute("allUsers", list10Users);

        return "index";
    }

}
