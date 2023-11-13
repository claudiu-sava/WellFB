package com.claudiusava.WellFB.controller;

import com.claudiusava.WellFB.dto.ChangePasswordDto;
import com.claudiusava.WellFB.model.*;
import com.claudiusava.WellFB.repository.AvatarRepository;
import com.claudiusava.WellFB.repository.PostRepository;
import com.claudiusava.WellFB.repository.RoleRepository;
import com.claudiusava.WellFB.repository.UserRepository;
import com.claudiusava.WellFB.service.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;

import static com.claudiusava.WellFB.WellFbApplication.*;
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
    @Autowired
    private AvatarRepository avatarRepository;
    @Autowired
    private Session session;


    @PostMapping("/new")
    private String newUser(@ModelAttribute User user){

        User userToDb = new User();
        userToDb.setUsername(user.getUsername());
        userToDb.setPassword(passwordEncoder().encode(user.getPassword()));

        Role roles = roleRepository.findByName("ROLE_USER").get();
        userToDb.setRoles(Collections.singleton(roles));

        userToDb.setPosts(null);
        userToDb.setFollows(null);
        userToDb.setFollowedBy(null);

        Avatar userAvatar = new Avatar();
        userAvatar.setFileName("/avatars/default_avatar_100.png");
        avatarRepository.save(userAvatar);

        userToDb.setAvatar(userAvatar);

        userRepository.save(userToDb);

        return "redirect:/login";
    }

    @GetMapping("/")
    private String getUserPage(@RequestParam(value = "username", required = false) String username,
                               RedirectAttributes redirectAttributes,
                               Model model){


        if(username == null){
            String usernameReplace = session.getLoggedUsername();
            redirectAttributes.addAttribute("username", usernameReplace);
            return "redirect:/users/";
        }


        Optional<User> userOptional = userRepository.findByUsername(username);

        if(userOptional.isEmpty()){
            return "redirect:/error";
        }

        User user = userOptional.get();
        User loggedUser = session.getLoggedUser();


        Iterable<Post> postsByUser = postRepository.findAllByUser(user);

        model.addAttribute("allPostsByUser", postsByUser);
        model.addAttribute("username", username);
        model.addAttribute("title", username);
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/edit")
    private String editUserPage(Model model,
                                @RequestParam(value = "username", required = false) String username,
                                RedirectAttributes redirectAttributes){



        User loggedUser = session.getLoggedUser();

        if(username == null || !username.equals(loggedUser.getUsername())){
            String usernameReplace = loggedUser.getUsername();
            redirectAttributes.addAttribute("username", usernameReplace);
            return "redirect:/users/edit";
        }

        model.addAttribute("title", "Edit: " + loggedUser.getUsername());
        model.addAttribute("loggedUser", loggedUser);

        return "editUser";
    }

    @PostMapping("/pswd")
    private String changePassword(@ModelAttribute ChangePasswordDto changePasswordDto,
                                  RedirectAttributes redirectAttributes){

        User loggedUser = session.getLoggedUser();

        if(passwordEncoder().matches(changePasswordDto.getOldPassword(), loggedUser.getPassword())){

            if (changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmNewPassword())){

                loggedUser.setPassword(passwordEncoder().encode(changePasswordDto.getNewPassword()));
                userRepository.save(loggedUser);

            } else {
                redirectAttributes.addAttribute("passwordDoesNotMatch", true);
                return "redirect:/users/edit?username=" + loggedUser.getUsername();
            }

        } else {
            redirectAttributes.addAttribute("wrongPassword", true);
            return "redirect:/users/edit?username=" + loggedUser.getUsername();
        }
        redirectAttributes.addAttribute("success", true);
        return "redirect:/users/edit?username=" + loggedUser.getUsername();
    }


    @PostMapping("/changeAvatar")
    private String changeAvatar(RedirectAttributes redirectAttributes,
                                @RequestParam("avatar") MultipartFile avatar) throws IOException{
        User loggedUser = session.getLoggedUser();
        Avatar oldAvatar = loggedUser.getAvatar();

        StringBuilder fileName = new StringBuilder();
        Path fileNameAndPath = Paths.get(AVATAR_DIRECTORY, avatar.getOriginalFilename());
        fileName.append(avatar.getOriginalFilename());
        Files.write(fileNameAndPath, avatar.getBytes());

        Avatar userAvatar = new Avatar();
        userAvatar.setFileName(AVATAR_BASE + fileName);
        avatarRepository.save(userAvatar);

        loggedUser.setAvatar(userAvatar);

        userRepository.save(loggedUser);

        avatarRepository.delete(oldAvatar);

        redirectAttributes.addAttribute("success", true);
        return "redirect:/users/edit?username=" + loggedUser.getUsername();
    }


}
