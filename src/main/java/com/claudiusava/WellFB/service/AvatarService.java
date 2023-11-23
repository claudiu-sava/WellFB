package com.claudiusava.WellFB.service;

import com.claudiusava.WellFB.model.Avatar;
import com.claudiusava.WellFB.repository.AvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvatarService {

    @Autowired
    private AvatarRepository avatarRepository;

    public void saveAvatar(Avatar avatar){

        avatarRepository.save(avatar);

    }

    public void deleteOldAvatar(Avatar avatar){

        avatarRepository.delete(avatar);

    }

}
