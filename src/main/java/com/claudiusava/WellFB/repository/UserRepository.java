package com.claudiusava.WellFB.repository;

import com.claudiusava.WellFB.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Iterable<User> findFirst10By();
}
