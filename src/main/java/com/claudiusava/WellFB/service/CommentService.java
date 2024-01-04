package com.claudiusava.WellFB.service;

import com.claudiusava.WellFB.model.Comment;
import com.claudiusava.WellFB.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public void saveComment(Comment comment){

        commentRepository.save(comment);

    }

    public Iterable<Comment> getAllComments(){

        return commentRepository.findAll();

    }

    public Comment getCommentById(int id){

        Optional<Comment> commentOptional = commentRepository.findById(id);
        if(commentOptional.isPresent()){
            return commentOptional.get();
        }

        return null;

    }

    public void deleteComment(int id){

        Comment comment = getCommentById(id);
        deleteComment(comment);

    }

    public void deleteComment(Comment comment){

        commentRepository.delete(comment);

    }

}
