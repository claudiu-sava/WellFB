package com.claudiusava.WellFB.controller;

import com.claudiusava.WellFB.model.Post;
import com.claudiusava.WellFB.model.User;
import com.claudiusava.WellFB.repository.PostRepository;
import com.claudiusava.WellFB.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/new")
    private String newPostPage(Model model){

        model.addAttribute("title", "Add new post");
        return "newPost";

    }

    @PostMapping("/new")
    private String newPost(Model model,
                           @ModelAttribute Post post){

        Post postToDb = new Post();
        postToDb.setLikes(0);
        postToDb.setDescription(post.getDescription());

        User user = userRepository.findByUsername(User.getLoggedUsername()).get();
        List<Post> userPost = user.getPosts();
        userPost.add(postToDb);

        postRepository.save(postToDb);
        userRepository.save(user);

        return "redirect:/";
    }

}
