package com.savemyroaming.restApi;

import com.savemyroaming.DTO.RegisterUserData;
import com.savemyroaming.DTO.UserDTO;
import com.savemyroaming.entities.UserEntity;
import com.savemyroaming.services.EmailVerificationService;
import com.savemyroaming.services.UserRegistrationService;
import com.savemyroaming.utils.UserGuard;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserRegistrationController implements UserRegistrationApi {
  @Autowired
  private ModelMapper dataMapper;


  @Autowired
  private UserGuard userGuard;
  @Autowired
  private UserRegistrationService userRegistrationService;

  @Autowired
  private EmailVerificationService emailVerificationService;


  public UserRegistrationController(ModelMapper dataMapper, UserRegistrationService userRegistrationService) {
    this.dataMapper = dataMapper;
    this.userRegistrationService = userRegistrationService;
  }

  @Override
  public ResponseEntity registerUser(UserDTO userDto, HttpServletRequest request, String recaptchaResponse)
          throws Exception {
    userGuard.validateRecaptcha(request, recaptchaResponse);

    UserEntity data = dataMapper.map(userDto, UserEntity.class);
    UserEntity newUser = userRegistrationService.saveNewUserData(new RegisterUserData(data, getSiteURL(request)));
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Override
  public String verifyUser(String code) {
    String verificationString;
    if (emailVerificationService.verify(code)) {

      verificationString = "<h3>Your E-mail has been verified.\n</h3>";
      return verificationString;

    } else {
      verificationString = " <h3>Something went wrong while verifying your E-mail, please contact support!\n</h3>";
      return verificationString;
    }
  }

  @Override
  public List<UserDTO> findByAll() {
    List<UserEntity> contatList = userRegistrationService.getAll();
    List<UserDTO> mapList = contatList.stream().map(person -> dataMapper.map(person, UserDTO.class))
            .collect(Collectors.toList());
    return mapList;

  }

  private String getSiteURL(HttpServletRequest request) {
    String siteURL = request.getRequestURL().toString();
    return siteURL.replace(request.getServletPath(), "");
  }


}
