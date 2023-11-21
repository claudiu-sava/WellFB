package com.claudiusava.WellFB.controller;
import com.claudiusava.WellFB.service.UserStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserStatusController {
    @Autowired
    private UserStatusService userStatusService;

    @GetMapping("/status")
    private void setUserStatus(){

        userStatusService.setUserStatus();

    }
}
