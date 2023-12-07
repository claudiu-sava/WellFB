package com.claudiusava.WellFB.dto;

import com.claudiusava.WellFB.model.Comment;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ModalPostDto {

    private String postDesc;
    private String hqUpload;
    private String user;
    private String avatar;
    private String postId;
    private boolean hasUserLiked;
    private List<Comment> comments;
    private boolean isUserAuthor;

}
