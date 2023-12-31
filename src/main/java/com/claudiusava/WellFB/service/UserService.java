package com.claudiusava.WellFB.service;

import com.claudiusava.WellFB.model.User;
import com.claudiusava.WellFB.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getUserById(int id){

        Optional<User> userOptional = userRepository.findById(id);

        return userOptional.orElse(null);

    }

    public void saveChangesToUser(User user){
        userRepository.save(user);
    }

    public List<User> searchUsersByUsername(String username){

        return userRepository.findAllByUsernameContaining(username);

    }

    public Iterable<User> getAllUsers(){

        return userRepository.findAll();

    }

    public User getUserByUsername(String username){

        Optional<User> userOptional = userRepository.findByUsername(username);

        if(userOptional.isPresent()){
            return userOptional.get();
        }

        return null;

    }

}
