package com.SaveMyRoaming.savemyroaming.services;

import com.SaveMyRoaming.savemyroaming.entities.UserEntity;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface UserRegistrationService {

    UserEntity saveNewUserData(UserEntity data, String siteURL) throws UnsupportedEncodingException, MessagingException;

}
