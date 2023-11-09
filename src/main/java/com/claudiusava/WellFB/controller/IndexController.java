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

import java.util.*;

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
        Iterable<User> allUsers = userRepository.findAll();

        List<User> allUsersList = new ArrayList<>();
        allUsers.forEach(allUsersList::add);

        Collections.shuffle(allUsersList);

        List<User> usersToShow = new ArrayList<>();

        for (int i = 0; i <= 9; i++){
            if (!allUsersList.get(i).equals(loggedUser)){
                usersToShow.add(allUsersList.get(i));
            }
        }


        model.addAttribute("title", "WellFB");
        model.addAttribute("loggedUser", loggedUser);

        model.addAttribute("allPosts", allPosts);
        model.addAttribute("allUsers", usersToShow);

        return "index";
    }

}
