package com.claudiusava.WellFB.controller.Admin;

import com.claudiusava.WellFB.model.Comment;
import com.claudiusava.WellFB.model.Post;
import com.claudiusava.WellFB.model.Role;
import com.claudiusava.WellFB.model.User;
import com.claudiusava.WellFB.service.CommentService;
import com.claudiusava.WellFB.service.PostService;
import com.claudiusava.WellFB.service.SessionService;
import com.claudiusava.WellFB.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private SessionService sessionService;
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;


    @GetMapping("/")
    public String admin(Model model){


        model.addAttribute("loggedUser", sessionService.getLoggedUser());
        model.addAttribute("title", "Admin Page");
        return "admin";

    }

    @GetMapping("/postsManagement")
    public String showPostsManagementPage(Model model){
        model.addAttribute("title", "Posts Management");

        Iterable<Post> posts = postService.getAllPostsDesc();

        model.addAttribute("loggedUser", sessionService.getLoggedUser());
        model.addAttribute("allPosts", posts);
        return "postsManagement";
    }

    @GetMapping("/usersManagement")
    public String showUsersManagementPage(Model model){
        model.addAttribute("title", "Users Management");
        model.addAttribute("loggedUser", sessionService.getLoggedUser());

        Iterable<User> allUsers = userService.getAllUsers();
        List<User> allUsersNonAdmin = new ArrayList<>();
        List<User> allUsersAdmin = new ArrayList<>();

        for (User user : allUsers){
            Role userRole = user.getRoles().iterator().next();
            if(!userRole.getName().equals("ROLE_ADMIN")){
                allUsersNonAdmin.add(user);
            }else {
                allUsersAdmin.add(user);
            }
        }

        model.addAttribute("allUsersNonAdmin", allUsersNonAdmin);
        model.addAttribute("allUsersAdmin", allUsersAdmin);
        return "usersManagement";
    }

    @GetMapping("/commentsManagement")
    public String showCommentsManagementPage(Model model){
        model.addAttribute("title", "Comments Management");

        Iterable<Comment> allComments = commentService.getAllComments();
        Iterable<Post> allPosts = postService.getAllPostsDesc();

        model.addAttribute("allPosts", allPosts);
        model.addAttribute("allComments", allComments);

        return "commentsManagement";
    }


}
