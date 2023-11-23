package com.claudiusava.WellFB.controller;

import com.claudiusava.WellFB.dto.LikeDto;
import com.claudiusava.WellFB.dto.PostEditDto;
import com.claudiusava.WellFB.model.Post;
import com.claudiusava.WellFB.model.Upload;
import com.claudiusava.WellFB.model.User;

import com.claudiusava.WellFB.service.PostService;
import com.claudiusava.WellFB.service.SessionService;
import com.claudiusava.WellFB.service.UploadService;
import com.claudiusava.WellFB.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.claudiusava.WellFB.WellFbApplication.UPLOAD_BASE;
import static com.claudiusava.WellFB.WellFbApplication.UPLOAD_DIRECTORY;

@Controller
@RequestMapping("/post")
public class PostController {
    @Autowired
    private SessionService sessionService;
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private UploadService uploadService;

    @GetMapping("/new")
    public String newPostPage(Model model){

        User loggedUser = sessionService.getLoggedUser();
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("title", "Add new post");

        return "newPost";

    }

    @PostMapping("/new")
    public String newPost(@ModelAttribute Post post,
                           @RequestParam(value = "upload", required = false) MultipartFile upload) throws IOException {

        if(upload.getOriginalFilename().isEmpty()){
            return "redirect:/post/new?error";
        }

        StringBuilder fileName = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, upload.getOriginalFilename());
        fileName.append(upload.getOriginalFilename());
        Files.write(fileNameAndPath, upload.getBytes());

        Upload uploadToDb = new Upload();
        uploadToDb.setFileName(UPLOAD_BASE + fileName);

        uploadService.saveUpload(uploadToDb);

        Post postToDb = new Post();
        postToDb.setLikedBy(null);
        postToDb.setDescription(post.getDescription());
        postToDb.setUploadFile(uploadToDb);

        User user = sessionService.getLoggedUser();
        List<Post> userPost = user.getPosts();
        postToDb.setUser(user);
        userPost.add(postToDb);

        postService.savePost(postToDb);
        userService.saveChangesToUser(user);

        return "redirect:/";
    }

    @PostMapping("/like")
    @ResponseBody
    public LikeDto likePost(@RequestParam("id") Integer postId) {

        Post post = postService.getPostById(postId);
        User user = sessionService.getLoggedUser();
        List<User> postLikedBy = post.getLikedBy();

        boolean liked = false;

        if(postLikedBy.contains(user)){
            postLikedBy.remove(user);

        } else {
            postLikedBy.add(user);
            liked = true;
        }

        post.setLikedBy(postLikedBy);
        postService.savePost(post);

        if (liked){
            return new LikeDto(true, postLikedBy.size());
        }

        return new LikeDto(false, postLikedBy.size());
    }

    @PostMapping("/delete")
    public String deletePost(@RequestParam("id") Integer postId){

        Post postToDelete = postService.getPostById(postId);

        if (postToDelete.getUser() == sessionService.getLoggedUser()){
            User user = userService.getUserById(postToDelete.getUser().getId());
            Upload upload = uploadService.getUploadById(postToDelete.getUploadFile().getId());

            List<Post> allUserPosts = user.getPosts();
            allUserPosts.remove(postToDelete);
            user.setPosts(allUserPosts);

            userService.saveChangesToUser(user);
            postService.deletePost(postToDelete);
            uploadService.deleteUpload(upload);

            return "redirect:/";
        }

        return "redirect:/error?somethingWentWrong";

    }

    @PostMapping("/edit")
    @ResponseBody
    public PostEditDto editPost(@ModelAttribute PostEditDto postEditDto){

        Post postToEdit = postService.getPostById(postEditDto.getId());

        if (sessionService.getLoggedUser() == postToEdit.getUser()){
            postToEdit.setDescription(postEditDto.getDesc());
            postToEdit.setId(postToEdit.getId());
            postToEdit.setDate(postToEdit.getDate());
            postToEdit.setLikedBy(postToEdit.getLikedBy());
            postToEdit.setUploadFile(postToEdit.getUploadFile());
            postToEdit.setUser(postToEdit.getUser());

            postService.savePost(postToEdit);

            return postEditDto;
        }

        return postEditDto; // unchanged post

    }

}
