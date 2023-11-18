package com.savemyroaming.services;

import com.savemyroaming.DTO.RegisterUserData;
import com.savemyroaming.entities.UserEntity;

import java.util.List;

public interface UserRegistrationService {

  UserEntity saveNewUserData(RegisterUserData registerUserData) throws Exception;

  List<UserEntity> getAll();
}
