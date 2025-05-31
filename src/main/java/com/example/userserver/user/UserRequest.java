package com.example.userserver.user;

import lombok.Getter;

@Getter
public class UserRequest {
    private String username;
    private String email;
    private String plainPassword;


}