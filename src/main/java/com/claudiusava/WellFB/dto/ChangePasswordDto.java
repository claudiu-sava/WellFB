package com.claudiusava.WellFB.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChangePasswordDto {

    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;

}
