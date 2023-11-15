package com.savemyroaming.restApi;

import com.savemyroaming.DTO.UserDTO;
import com.savemyroaming.capatcha.CaptchaService;
import com.savemyroaming.capatcha.RecaptchaArgument;
import com.savemyroaming.entities.UserEntity;
import com.savemyroaming.services.EmailVerificationService;
import com.savemyroaming.services.UserRegistrationService;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class UserRegistrationController implements UserRegistrationApi {


  @Autowired
  private ModelMapper dataMapper;

  @Autowired
  private UserRegistrationService userRegistrationService;

  @Autowired
  private EmailVerificationService emailVerificationService;

  @Autowired
  private CaptchaService captchaService;


  public UserRegistrationController(ModelMapper dataMapper, UserRegistrationService userRegistrationService) {
    this.dataMapper = dataMapper;
    this.userRegistrationService = userRegistrationService;
  }

  @Override
  public ResponseEntity registerUser(UserDTO userDto, HttpServletRequest request, String recaptchaResponse)
          throws Exception {
    //capatcha
    String ip = request.getRemoteAddr();
    RecaptchaArgument recaptchaArguments = new RecaptchaArgument();
    recaptchaArguments.setIp(ip);
    recaptchaArguments.setRecaptchaResponse(recaptchaResponse);
//        String VerifyMessage =
//                (String)captchaService.verifyCaptcha(recaptchaResponse);
    String captchaVerifyMessage =
            (String) captchaService.verifyCaptcha(recaptchaArguments);

    if (StringUtils.isNotEmpty(captchaVerifyMessage)) {
      Map<String, Object> response = new HashMap<>();
      response.put("message", captchaVerifyMessage);
      return ResponseEntity.badRequest()
              .body(response);
    }

    UserEntity data = dataMapper.map(userDto, UserEntity.class);
    UserEntity newUser = userRegistrationService.saveNewUserData(data, getSiteURL(request));
    UserDTO createdUser = dataMapper.map(newUser, UserDTO.class);
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
