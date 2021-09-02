package com.SaveMyRoaming.savemyroaming.restApi;


import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.SaveMyRoaming.savemyroaming.DTO.UserDTO;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RequestMapping(value = "/user")

public interface UserRegistrationApi {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
   ResponseEntity registerUser( @Valid @RequestBody UserDTO userDto , HttpServletRequest request, @RequestParam(name="g-recaptcha-response") String recaptchaResponse) throws UnsupportedEncodingException, MessagingException;


    @GetMapping("/verify")
    String verifyUser(@Param ("code") String code) ;


}
