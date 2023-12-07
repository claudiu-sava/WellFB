package com.claudiusava.WellFB.repository;

import com.claudiusava.WellFB.model.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Integer> {
}
