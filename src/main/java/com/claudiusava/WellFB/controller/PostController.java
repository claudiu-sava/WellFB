package com.claudiusava.WellFB.controller;

import com.claudiusava.WellFB.model.Post;
import com.claudiusava.WellFB.model.Upload;
import com.claudiusava.WellFB.model.User;
import com.claudiusava.WellFB.repository.PostRepository;
import com.claudiusava.WellFB.repository.UploadRepository;
import com.claudiusava.WellFB.repository.UserRepository;
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

@Controller
@RequestMapping("/post")
public class PostController {
    public static final String UPLOAD_BASE = "/uploads";
    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static" + UPLOAD_BASE;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UploadRepository uploadRepository;

    @GetMapping("/new")
    private String newPostPage(Model model){

        model.addAttribute("title", "Add new post");
        return "newPost";

    }

    @PostMapping("/new")
    private String newPost(Model model,
                           @ModelAttribute Post post,
                           @RequestParam("upload") MultipartFile upload) throws IOException {

        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, upload.getOriginalFilename());
        fileNames.append(upload.getOriginalFilename());
        Files.write(fileNameAndPath, upload.getBytes());

        Upload uploadToDb = new Upload();
        uploadToDb.setFileName(UPLOAD_BASE + "/" + fileNames);
        uploadRepository.save(uploadToDb);

        Post postToDb = new Post();
        postToDb.setLikes(0);
        postToDb.setDescription(post.getDescription());
        postToDb.setUploadFile(uploadToDb);

        User user = userRepository.findByUsername(User.getLoggedUsername()).get();
        List<Post> userPost = user.getPosts();
        postToDb.setUser(user);
        userPost.add(postToDb);


        postRepository.save(postToDb);
        userRepository.save(user);

        return "redirect:/";
    }

}
