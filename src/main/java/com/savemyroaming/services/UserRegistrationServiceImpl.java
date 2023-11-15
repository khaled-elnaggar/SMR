package com.savemyroaming.services;

import com.savemyroaming.entities.UserEntity;
import com.savemyroaming.repositories.UserRepository;
import com.savemyroaming.utils.Constants;
import com.savemyroaming.utils.DateConverter;
import net.bytebuddy.utility.RandomString;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.management.RuntimeErrorException;
import java.util.List;


@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

  @Autowired
  private EmailVerificationService emailVerificationService;

  private UserRepository userRepo;


  public UserRegistrationServiceImpl(UserRepository userRepo) {
    this.userRepo = userRepo;
  }

  @Override
  public UserEntity saveNewUserData(UserEntity data, String siteURL) throws Exception {
    userDataNullOrEmptyValidation(data);
    setDataForEmailOrThrowIfInvalid(data);
    UserEntity newUser = userRepo.save(data);
    try {
      emailVerificationService.sendVerificationEmail(data, siteURL);
      System.out.println("Email sent.");
    } catch (Exception ex) {
      System.out.println("Failed to sent email.");
    }

    return newUser;
  }

  @Override
  public List<UserEntity> getAll() {
    return userRepo.findAll();
  }

  private void setDataForEmailOrThrowIfInvalid(UserEntity data) throws Exception {
    setDataForValidEmail(data);
  }

  private void setDataForValidEmail(UserEntity data) {
    UserEntity userEmail = userRepo.findByEmail(data.getEmail());
    checkIfUniqueEmailOrThrow(data, userEmail);
    data.setTimezone(getDate());
    String randomCode = RandomString.make(4);
    data.setVerificationCode(randomCode);
  }

  public void userDataNullOrEmptyValidation(UserEntity data) {
    throwIfStringIsEmptyOrNull(data.getFirst_name(), Constants.MSG_ERROR_EMPTY_USER_FIRSTNAME);
    throwIfStringIsEmptyOrNull(data.getLast_name(), Constants.MSG_ERROR_EMPTY_USER_LASTNAME);
    throwIfStringIsEmptyOrNull(data.getEmail(), Constants.MSG_ERROR_EMPTY_USEREmail);
  }

  private void throwIfStringIsEmptyOrNull(String str, String error) {
    if (str == null || str.isBlank())
      throw new RuntimeErrorException(null, error);
  }


  private void checkIfUniqueEmailOrThrow(UserEntity data, UserEntity userEmail) {
    if (userEmail != null && data.getEmail().equals(userEmail.getEmail()))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constants.MSG_ERROR_EMPTY_USEREMAIL);
  }

  private String getDate() {
    DateTime now = DateTime.now(DateTimeZone.UTC);
    String intervalDate = DateConverter.formatDate(now.toDate(), DateTimeZone.UTC);
    return intervalDate;
  }

}
