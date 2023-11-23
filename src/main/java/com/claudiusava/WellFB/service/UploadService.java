package com.claudiusava.WellFB.service;

import com.claudiusava.WellFB.model.Upload;
import com.claudiusava.WellFB.repository.UploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UploadService {

    @Autowired
    private UploadRepository uploadRepository;

    public Upload getUploadById(int id){

        Optional<Upload> uploadOptional = uploadRepository.findById(id);

        if (uploadOptional.isPresent()){
            return uploadOptional.get();
        }

        return null;

    }

    public void deleteUpload(Upload upload){

        uploadRepository.delete(upload);

    }

    public void saveUpload(Upload upload){

        uploadRepository.save(upload);

    }
}
