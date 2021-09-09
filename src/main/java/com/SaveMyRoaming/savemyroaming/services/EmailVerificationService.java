package com.SaveMyRoaming.savemyroaming.services;

import com.SaveMyRoaming.savemyroaming.entities.UserEntity;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface EmailVerificationService {


    public void sendVerificationEmail(UserEntity user, String siteURL) throws MessagingException, UnsupportedEncodingException;
    public boolean verify(String verificationCode);
}
