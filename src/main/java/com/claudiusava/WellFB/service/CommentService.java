package com.claudiusava.WellFB.service;

import com.claudiusava.WellFB.model.Comment;
import com.claudiusava.WellFB.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public void saveComment(Comment comment){

        commentRepository.save(comment);

    }

}
