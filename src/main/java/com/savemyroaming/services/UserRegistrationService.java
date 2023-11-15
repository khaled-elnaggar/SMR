package com.savemyroaming.services;

import com.savemyroaming.entities.UserEntity;

import java.util.List;

public interface UserRegistrationService {

  UserEntity saveNewUserData(UserEntity data, String siteURL) throws Exception;

  List<UserEntity> getAll();
}
