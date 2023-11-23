package com.claudiusava.WellFB.repository;

import com.claudiusava.WellFB.model.Post;
import com.claudiusava.WellFB.model.User;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Integer> {
    Iterable<Post> findAllByUser(User user);
    Iterable<Post> findAllByOrderByDateDesc();
}
