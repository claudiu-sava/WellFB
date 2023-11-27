package com.claudiusava.WellFB.repository;

import com.claudiusava.WellFB.model.Post;
import com.claudiusava.WellFB.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Integer> {
    List<Post> findAllByUser(User user);
    Iterable<Post> findAllByOrderByDateDesc();
}
