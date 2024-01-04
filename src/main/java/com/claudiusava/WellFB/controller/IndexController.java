package com.claudiusava.WellFB.controller;

import com.claudiusava.WellFB.model.Post;
import com.claudiusava.WellFB.model.User;
import com.claudiusava.WellFB.service.PostService;
import com.claudiusava.WellFB.service.SessionService;
import com.claudiusava.WellFB.service.UserService;
import com.claudiusava.WellFB.service.UserStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@Controller
public class IndexController {
    @Autowired
    private SessionService sessionService;
    @Autowired
    private UserStatusService userStatusService;
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;


    @GetMapping("/")
    public String showIndexPage(Model model){

        User loggedUser = sessionService.getLoggedUser();

        Iterable<Post> allPosts = postService.getAllPostsDesc();
        Iterable<User> allUsers = userService.getAllUsers();

        List<User> allUsersList = new ArrayList<>();
        allUsers.forEach(allUsersList::add);

        Collections.shuffle(allUsersList);

        List<User> usersToShow = new ArrayList<>();

        if(allUsersList.size() >= 9){
            for (int i = 0; i <= 9; i++){
                if (!allUsersList.get(i).equals(loggedUser)){
                    usersToShow.add(allUsersList.get(i));
                }
            }
        }


        model.addAttribute("title", "WellFB");
        model.addAttribute("loggedUser", loggedUser);

        model.addAttribute("allPosts", allPosts);
        model.addAttribute("allUsers", usersToShow);
        model.addAttribute("userStatusService", userStatusService);

        return "index";
    }

}
