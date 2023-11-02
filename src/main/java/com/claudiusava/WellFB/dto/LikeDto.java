package com.claudiusava.WellFB.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LikeDto {

    private boolean liked;
    private Integer likes;

    public LikeDto(Boolean liked, Integer likes){
        this.liked = liked;
        this.likes = likes;
    }
}
