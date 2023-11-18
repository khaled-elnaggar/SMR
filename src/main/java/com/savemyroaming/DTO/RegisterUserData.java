package com.savemyroaming.DTO;

import com.savemyroaming.entities.UserEntity;

public class RegisterUserData {
  private final UserEntity data;
  private final String siteURL;

  public RegisterUserData(UserEntity data, String siteURL) {
    this.data = data;
    this.siteURL = siteURL;
  }

  public UserEntity getData() {
    return data;
  }

  public String getSiteURL() {
    return siteURL;
  }
}
