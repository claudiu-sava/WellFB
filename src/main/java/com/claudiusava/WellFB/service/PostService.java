package com.claudiusava.WellFB.service;

import com.claudiusava.WellFB.model.Post;
import com.claudiusava.WellFB.model.User;
import com.claudiusava.WellFB.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public void savePost(Post post){

        postRepository.save(post);

    }

    public Post getPostById(int id){

        Optional<Post> postOptional = postRepository.findById(id);
        if(postOptional.isPresent()){
            return postOptional.get();
        }

        return null;

    }

    public void deletePost(Post post){

        postRepository.delete(post);

    }

    public List<Post> getAllPostsByUser(User user){

        return postRepository.findAllByUser(user);

    }

    public Iterable<Post> getAllPostsDesc(){

        return postRepository.findAllByOrderByDateDesc();

    }
}
