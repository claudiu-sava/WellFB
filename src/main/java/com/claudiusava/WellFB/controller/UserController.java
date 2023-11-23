package com.claudiusava.WellFB.controller;

import com.claudiusava.WellFB.dto.ChangePasswordDto;
import com.claudiusava.WellFB.dto.FollowUserDto;
import com.claudiusava.WellFB.model.*;
import com.claudiusava.WellFB.service.*;
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
import java.util.List;

import static com.claudiusava.WellFB.WellFbApplication.*;
import static com.claudiusava.WellFB.security.SecurityConfiguration.passwordEncoder;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private SessionService sessionService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserStatusService userStatusService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AvatarService avatarService;
    @Autowired
    private PostService postService;

    private static final String USER_EDIT_REDIRECT = "redirect:/users/edit?username=";

    @PostMapping("/new")
    public String newUser(@ModelAttribute User user){

        User userToDb = new User();
        userToDb.setUsername(user.getUsername());
        userToDb.setPassword(passwordEncoder().encode(user.getPassword()));

        Role roles = roleService.getRoleByName("ROLE_USER");
        userToDb.setRoles(Collections.singleton(roles));

        userToDb.setPosts(null);
        userToDb.setFollows(null);
        userToDb.setFollowedBy(null);

        Avatar userAvatar = new Avatar();
        userAvatar.setFileName("/avatars/default_avatar_100.png");
        avatarService.saveAvatar(userAvatar);

        userToDb.setAvatar(userAvatar);

        userService.saveChangesToUser(userToDb);

        return "redirect:/login";
    }

    @GetMapping("/")
    public String getUserPage(@RequestParam(value = "username", required = false) String username,
                               Model model){

        User loggedUser = sessionService.getLoggedUser();

        if(username == null){
            return "redirect:/users/?username=" + loggedUser.getUsername();
        }

        User user = userService.getUserByUsername(username);

        if(user == null){
            return "redirect:/error?userNotFound";
        }

        Iterable<Post> postsByUser = postService.getAllPostsByUser(user);

        model.addAttribute("allPostsByUser", postsByUser);
        model.addAttribute("username", username);
        model.addAttribute("title", username);
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("user", user);
        model.addAttribute("userStatusService", userStatusService);
        return "user";
    }

    @GetMapping("/edit")
    public String editUserPage(Model model,
                                @RequestParam(value = "username", required = false) String username,
                                RedirectAttributes redirectAttributes){



        User loggedUser = sessionService.getLoggedUser();

        if(username == null || !username.equals(loggedUser.getUsername())){
            String usernameReplace = loggedUser.getUsername();
            redirectAttributes.addAttribute("username", usernameReplace);
            return "redirect:/users/edit";
        }

        model.addAttribute("title", "Edit: " + loggedUser.getUsername());
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("userStatusService", userStatusService);

        return "editUser";
    }

    @PostMapping("/pswd")
    public String changePassword(@ModelAttribute ChangePasswordDto changePasswordDto){

        User loggedUser = sessionService.getLoggedUser();

        if(passwordEncoder().matches(changePasswordDto.getOldPassword(), loggedUser.getPassword())){

            if (changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmNewPassword())){

                loggedUser.setPassword(passwordEncoder().encode(changePasswordDto.getNewPassword()));
                userService.saveChangesToUser(loggedUser);

            } else {
                return USER_EDIT_REDIRECT + loggedUser.getUsername() + "&passwordDoesNotMatch";
            }

        } else {
            return USER_EDIT_REDIRECT + loggedUser.getUsername() + "&wrongPassword";
        }
        return USER_EDIT_REDIRECT + loggedUser.getUsername() + "&success";
    }


    @PostMapping("/changeAvatar")
    public String changeAvatar(@RequestParam("avatar") MultipartFile avatar) throws IOException{

        User loggedUser = sessionService.getLoggedUser();

        if (avatar.getOriginalFilename().isEmpty()) {
            return USER_EDIT_REDIRECT + loggedUser.getUsername() + "&noNewAvatar";
        }

        Avatar oldAvatar = loggedUser.getAvatar();

        StringBuilder fileName = new StringBuilder();
        Path fileNameAndPath = Paths.get(AVATAR_DIRECTORY, avatar.getOriginalFilename());
        fileName.append(avatar.getOriginalFilename());
        Files.write(fileNameAndPath, avatar.getBytes());

        Avatar userAvatar = new Avatar();
        userAvatar.setFileName(AVATAR_BASE + fileName);
        avatarService.saveAvatar(userAvatar);

        loggedUser.setAvatar(userAvatar);

        userService.saveChangesToUser(loggedUser);

        avatarService.deleteOldAvatar(oldAvatar);

        return USER_EDIT_REDIRECT + loggedUser.getUsername() + "&success";
    }

    @PostMapping("/followUser")
    @ResponseBody
    public FollowUserDto followUser(@RequestParam("id") Integer id){

        User loggedUser = sessionService.getLoggedUser();
        User userWhoGetsFollowed = userService.getUserById(id);

        if (userWhoGetsFollowed == null){
            return null;
        }

        List<User> loggedUserFollowsList = loggedUser.getFollows();
        List<User> userWhoGetsFollowedFollowedByList = userWhoGetsFollowed.getFollowedBy();

        boolean isFollowing = false;

        if(loggedUser.getFollows().contains(userWhoGetsFollowed)){
            loggedUserFollowsList.remove(userWhoGetsFollowed);
            userWhoGetsFollowedFollowedByList.remove(loggedUser);
        } else {
            loggedUserFollowsList.add(userWhoGetsFollowed);
            userWhoGetsFollowedFollowedByList.add(loggedUser);
            isFollowing = true;
        }

        loggedUser.setFollows(loggedUserFollowsList);
        userWhoGetsFollowed.setFollowedBy(userWhoGetsFollowedFollowedByList);

        userService.saveChangesToUser(loggedUser);
        userService.saveChangesToUser(userWhoGetsFollowed);

        FollowUserDto followUserDto = new FollowUserDto();
        followUserDto.setIsFollowing(isFollowing);
        followUserDto.setFollowersCount(userWhoGetsFollowedFollowedByList.size());

        return followUserDto;

    }

    @GetMapping("/search")
    public String searchUserPage(@RequestParam("username") String username,
                                 Model model){

        List<User> userList = userService.searchUsersByUsername(username);

        model.addAttribute("title", "Search for " + username);
        model.addAttribute("userList", userList);
        model.addAttribute("loggedUser", sessionService.getLoggedUser());
        model.addAttribute("userStatusService", userStatusService);

        return "userSearch";

    }


}
