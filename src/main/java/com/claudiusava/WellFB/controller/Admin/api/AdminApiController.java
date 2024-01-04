package com.claudiusava.WellFB.controller.Admin.api;

import com.claudiusava.WellFB.model.Post;
import com.claudiusava.WellFB.model.Role;
import com.claudiusava.WellFB.model.User;
import com.claudiusava.WellFB.service.PostService;
import com.claudiusava.WellFB.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/admin/api")
public class AdminApiController {

    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

    @GetMapping("/getAllUsers")
    public HashMap<String, Integer> getAllUsers(){

        int totalUsers = 0;
        int onlineUsers = 0;
        Iterable<User> allUsers = userService.getAllUsers();

        for(User user : allUsers){
            if(user.getIsOnline() != null && user.getIsOnline()){
                onlineUsers ++;
            }
            totalUsers ++;
        }

        HashMap<String, Integer> response = new HashMap<>();
        response.put("totalUsers", totalUsers);
        response.put("onlineUsers", onlineUsers);

        return response;

    }

    @GetMapping("/getAllPosts")
    public HashMap<String,Integer> getAllPosts(){

        Iterable<Post> allPosts = postService.getAllPostsDesc();
        int totalPosts = 0;
        int postsWithoutComments = 0;
        int postsWithoutLikes = 0;

        for (Post post : allPosts){
            if(post.getComments().isEmpty()){
                postsWithoutComments ++;
            }
            if(post.getLikedBy().isEmpty()){
                postsWithoutLikes ++;
            }

            totalPosts ++;
        }

        HashMap<String, Integer> response = new HashMap<>();
        response.put("totalPosts", totalPosts);
        response.put("postsWithoutComments", postsWithoutComments);
        response.put("postsWithoutLikes", postsWithoutLikes);

        return response;

    }

    @GetMapping("/postFrequency")
    public HashMap<String, Integer> getPostFrequency(){

        Iterable<Post> allPosts = postService.getAllPostsDesc();

        List<Integer> monthsList = new ArrayList<>();
        for (Post post : allPosts){
            String[] postDate = post.getDate().split("\\.");

            monthsList.add(Integer.parseInt(postDate[1]));

        }

        HashMap<String, Integer> answer = new HashMap<>();
        answer.put("jan", Collections.frequency(monthsList, 1));
        answer.put("feb", Collections.frequency(monthsList, 2));
        answer.put("mar", Collections.frequency(monthsList, 3));
        answer.put("apr", Collections.frequency(monthsList, 4));
        answer.put("may", Collections.frequency(monthsList, 5));
        answer.put("jun", Collections.frequency(monthsList, 6));
        answer.put("jul", Collections.frequency(monthsList, 7));
        answer.put("aug", Collections.frequency(monthsList, 8));
        answer.put("sep", Collections.frequency(monthsList, 9));
        answer.put("oct", Collections.frequency(monthsList, 10));
        answer.put("nov", Collections.frequency(monthsList, 11));
        answer.put("dec", Collections.frequency(monthsList, 12));
        return answer;

    }

    @GetMapping("/adminCounter")
    public HashMap<String, Integer> adminCounter(){

        int totalUsers = 0;
        int totalAdmins = 0;
        Iterable<User> allUsers = userService.getAllUsers();

        for (User user : allUsers){
            Role userRole = user.getRoles().iterator().next();
            if(!userRole.getName().equals("ROLE_ADMIN")){
                totalUsers ++;
            }else {
                totalAdmins ++;
            }
        }

        HashMap<String, Integer> answer = new HashMap<>();
        answer.put("totalUsers", totalUsers);
        answer.put("totalAdmins", totalAdmins);

        return answer;

    }

    @GetMapping("/userFrequency")
    public HashMap<String, Integer> userFrequency(){

        Iterable<User> allUsers = userService.getAllUsers();
        List<Integer> monthsList = new ArrayList<>();
        for (User user : allUsers){

            String[] userCreationDate = user.getCreationDate().split("\\.");
            monthsList.add(Integer.parseInt(userCreationDate[1]));

        }

        HashMap<String, Integer> answer = new HashMap<>();
        answer.put("jan", Collections.frequency(monthsList, 1));
        answer.put("feb", Collections.frequency(monthsList, 2));
        answer.put("mar", Collections.frequency(monthsList, 3));
        answer.put("apr", Collections.frequency(monthsList, 4));
        answer.put("may", Collections.frequency(monthsList, 5));
        answer.put("jun", Collections.frequency(monthsList, 6));
        answer.put("jul", Collections.frequency(monthsList, 7));
        answer.put("aug", Collections.frequency(monthsList, 8));
        answer.put("sep", Collections.frequency(monthsList, 9));
        answer.put("oct", Collections.frequency(monthsList, 10));
        answer.put("nov", Collections.frequency(monthsList, 11));
        answer.put("dec", Collections.frequency(monthsList, 12));
        return answer;
    }

}
