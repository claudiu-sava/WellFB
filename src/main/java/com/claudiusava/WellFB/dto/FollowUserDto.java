package com.claudiusava.WellFB.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowUserDto {
    private Boolean isFollowing;
    private Integer followersCount;
}
