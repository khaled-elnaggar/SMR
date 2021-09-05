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
import java.util.List;

@RequestMapping(value = "/user")

public interface UserRegistrationApi {

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
   ResponseEntity registerUser( @Valid @RequestBody UserDTO userDto , HttpServletRequest request, @RequestParam String recaptchaResponse) throws Exception;


    @GetMapping("/verify")
    String verifyUser(@RequestParam ("code") String code) ;

// test db in deployement
    @GetMapping
    List<UserDTO> findByAll();

}
