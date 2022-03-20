package com.bestSite.service;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserRegistration {

    private Long id;

    @NotBlank(message = "UserName should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String username;

    @NotBlank(message = "First name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String firstName;

    @NotBlank(message = "Last name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String lastName;

    @NotBlank(message = "Name should not be empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String repeatPassword;
}
