package com.claudiusava.WellFB.controller;

import com.claudiusava.WellFB.dto.LikeDto;
import com.claudiusava.WellFB.dto.ModalPostDto;
import com.claudiusava.WellFB.dto.NewPostDto;
import com.claudiusava.WellFB.dto.PostEditDto;
import com.claudiusava.WellFB.model.Comment;
import com.claudiusava.WellFB.model.Post;
import com.claudiusava.WellFB.model.Upload;
import com.claudiusava.WellFB.model.User;

import com.claudiusava.WellFB.service.*;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static com.claudiusava.WellFB.WellFbApplication.*;

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
    @Autowired
    private CommentService commentService;

    @PostMapping("/new")
    public String newPost(@ModelAttribute Post post,
                           @RequestParam(value = "upload", required = false) MultipartFile upload) throws IOException {

        StringBuilder fileName = new StringBuilder();
        Path fileNameAndPath = Paths.get(HQ_UPLOAD_DIRECTORY, upload.getOriginalFilename());
        fileName.append(upload.getOriginalFilename());
        Files.write(fileNameAndPath, upload.getBytes());

        Upload uploadToDb = new Upload();
        uploadToDb.setFileName(UPLOAD_BASE + fileName);
        uploadToDb.setHqFileName(HQ_UPLOAD_BASE + fileName);

        Thumbnails.of(fileNameAndPath.toString()).size(1200, 800).outputQuality(1.0).toFiles(new File(UPLOAD_DIRECTORY), Rename.NO_CHANGE);

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

            File postToDeleteFromHdd = new File(postToDelete.getUploadFile().getFileName());
            postToDeleteFromHdd.delete();

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

    @GetMapping("/getPost")
    @ResponseBody
    public ModalPostDto getPostById(@RequestParam("id") int id){

        Post post = postService.getPostById(id);
        ModalPostDto modalPostDto = new ModalPostDto();
        modalPostDto.setPostDesc(post.getDescription());
        modalPostDto.setUser(post.getUser().getUsername());
        modalPostDto.setAvatar(post.getUser().getAvatar().getFileName());
        modalPostDto.setHqUpload(post.getUploadFile().getHqFileName());
        modalPostDto.setPostId(post.getId().toString());
        if(post.getUser() == sessionService.getLoggedUser()){
            modalPostDto.setUserAuthor(true);
        } else {
            modalPostDto.setUserAuthor(false);
        }


        if(post.getLikedBy().contains(sessionService.getLoggedUser())){
            modalPostDto.setHasUserLiked(true);
        } else {
            modalPostDto.setHasUserLiked(false);
        }

        return modalPostDto;

    }

    @PostMapping("/comment")
    @ResponseBody
    public String postComment(@RequestBody NewPostDto newPostDto){

        Post post = postService.getPostById(newPostDto.getPostId());


        Comment comment = new Comment();
        comment.setComment(newPostDto.getCommentBody());
        comment.setUser(sessionService.getLoggedUser());

        commentService.saveComment(comment);

        postService.addComment(post, comment);
        postService.savePost(post);

        return "success";
    }

    @GetMapping("/getComments")
    public String getComments(@RequestParam("id") int id,
                              Model model){

        Post post = postService.getPostById(id);
        List<Comment> comments = post.getComments();
        Collections.reverse(comments);

        model.addAttribute("comments", comments);
        return "/fragments/comments";
    }

}
