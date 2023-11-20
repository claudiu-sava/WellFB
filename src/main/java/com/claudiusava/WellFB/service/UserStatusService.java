package com.claudiusava.WellFB.service;

import com.claudiusava.WellFB.model.User;
import com.claudiusava.WellFB.model.UserStatus;
import com.claudiusava.WellFB.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class UserStatusService {
    @Autowired
    private SessionService sessionService;
    @Autowired
    private UserStatusRepository userStatusRepository;
    @Autowired
    private UserService userService;

    public void setUserStatus(){

        User loggedUser = sessionService.getLoggedUser();

        UserStatus userStatus = new UserStatus();
        userStatus.setUsername(loggedUser.getUsername());
        userStatus.setTimestamp(LocalDateTime.now());

        loggedUser.setIsOnline(true);

        userService.saveChangesToUser(loggedUser);
        userStatusRepository.save(userStatus);

    }

    public Boolean isUserOnline(User user){

        LocalDateTime actualTimestamp = LocalDateTime.now();

        if(userStatusRepository.findFirstByUsernameOrderByTimestampDesc(user.getUsername()).isPresent() && Duration.between(userStatusRepository.findFirstByUsernameOrderByTimestampDesc(user.getUsername()).get().getTimestamp(), actualTimestamp).toMinutes() < 10){
            user.setIsOnline(true);
            userService.saveChangesToUser(user);
            return true;
        } else {
            user.setIsOnline(false);
            userService.saveChangesToUser(user);
            return false;
        }

    }

}
