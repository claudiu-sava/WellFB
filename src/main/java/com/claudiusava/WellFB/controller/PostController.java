package com.claudiusava.WellFB.controller;

import com.claudiusava.WellFB.dto.LikeDto;
import com.claudiusava.WellFB.dto.PostEditDto;
import com.claudiusava.WellFB.model.Post;
import com.claudiusava.WellFB.model.Upload;
import com.claudiusava.WellFB.model.User;
import com.claudiusava.WellFB.repository.PostRepository;
import com.claudiusava.WellFB.repository.UploadRepository;
import com.claudiusava.WellFB.repository.UserRepository;
import com.claudiusava.WellFB.service.SessionService;
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
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UploadRepository uploadRepository;

    @GetMapping("/new")
    private String newPostPage(Model model){

        User loggedUser = sessionService.getLoggedUser();
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("title", "Add new post");

        return "newPost";

    }

    @PostMapping("/new")
    private String newPost(@ModelAttribute Post post,
                           @RequestParam("upload") MultipartFile upload) throws IOException {

        StringBuilder fileName = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, upload.getOriginalFilename());
        fileName.append(upload.getOriginalFilename());
        Files.write(fileNameAndPath, upload.getBytes());

        Upload uploadToDb = new Upload();
        uploadToDb.setFileName(UPLOAD_BASE + fileName);

        uploadRepository.save(uploadToDb);

        Post postToDb = new Post();
        postToDb.setLikedBy(null);
        postToDb.setDescription(post.getDescription());
        postToDb.setUploadFile(uploadToDb);

        User user = sessionService.getLoggedUser();
        List<Post> userPost = user.getPosts();
        postToDb.setUser(user);
        userPost.add(postToDb);


        postRepository.save(postToDb);
        userRepository.save(user);

        return "redirect:/";
    }

    @PostMapping("/like")
    @ResponseBody
    public LikeDto likePost(@RequestParam("id") Integer postId) {

        Post post = postRepository.findById(postId).get();
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
        postRepository.save(post);

        if (liked){
            return new LikeDto(true, postLikedBy.size());
        }

        return new LikeDto(false, postLikedBy.size());
    }

    @PostMapping("/delete")
    private String deletePost(@RequestParam("id") Integer postId){

        Post postToDelete = postRepository.findById(postId).get();
        User user = userRepository.findById(postToDelete.getUser().getId()).get();
        Upload upload = uploadRepository.findById(postToDelete.getUploadFile().getId()).get();

        List<Post> allUserPosts = user.getPosts();
        allUserPosts.remove(postToDelete);
        user.setPosts(allUserPosts);

        userRepository.save(user);
        postRepository.delete(postToDelete);
        uploadRepository.delete(upload);
        System.out.println("Post deleted");
        
        return "redirect:/";
    }

    @PostMapping("/edit")
    @ResponseBody
    private PostEditDto editPost(@ModelAttribute PostEditDto postEditDto){

        Post postToEdit = postRepository.findById(postEditDto.getId()).get();

        postToEdit.setDescription(postEditDto.getDesc());
        postToEdit.setId(postToEdit.getId());
        postToEdit.setDate(postToEdit.getDate());
        postToEdit.setLikedBy(postToEdit.getLikedBy());
        postToEdit.setUploadFile(postToEdit.getUploadFile());
        postToEdit.setUser(postToEdit.getUser());

        postRepository.save(postToEdit);

        return postEditDto;
    }

}
