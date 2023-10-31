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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.Optional;

import static com.claudiusava.WellFB.security.SecurityConfiguration.passwordEncoder;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;


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

    @GetMapping("/")
    private String getUserPage(@RequestParam(value = "username", required = false) String username,
                               RedirectAttributes redirectAttributes,
                               Model model){


        if(username == null){
            String usernameReplace = User.getLoggedUsername();
            redirectAttributes.addAttribute("username", usernameReplace);
            return "redirect:/users/";
        }


        Optional<User> userOptional = userRepository.findByUsername(username);

        if(userOptional.isEmpty()){
            return "redirect:/error";
        }

        User user = userOptional.get();
        User loggedUser = userRepository.findByUsername(User.getLoggedUsername()).get();


        Iterable<Post> postsByUser = postRepository.findAllByUser(user);

        model.addAttribute("allPostsByUser", postsByUser);
        model.addAttribute("username", username);
        model.addAttribute("title", username);
        model.addAttribute("loggedUser", loggedUser);
        return "user";
    }



}
