package com.jdcam.microservices.courses.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {

    private Long id;
    private String name;
    private String password;
    private String email;
}
