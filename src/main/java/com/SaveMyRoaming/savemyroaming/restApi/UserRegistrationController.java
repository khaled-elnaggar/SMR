package com.SaveMyRoaming.savemyroaming.restApi;

import com.SaveMyRoaming.savemyroaming.DTO.UserDTO;
import com.SaveMyRoaming.savemyroaming.capatcha.CaptchaService;
import com.SaveMyRoaming.savemyroaming.capatcha.RecaptchaArgument;
import com.SaveMyRoaming.savemyroaming.capatcha.RecaptchaService;
import com.SaveMyRoaming.savemyroaming.entities.UserEntity;
import com.SaveMyRoaming.savemyroaming.services.EmailVerificationService;
import com.SaveMyRoaming.savemyroaming.services.UserRegistrationService;
import com.SaveMyRoaming.savemyroaming.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

@RestController
@Slf4j
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
    public ResponseEntity <UserDTO>registerUser(UserDTO userDto , HttpServletRequest request, String recaptchaResponse)
            throws Exception {
        //capatcha
        String ip = request.getRemoteAddr();
        RecaptchaArgument recaptchaArguments = new RecaptchaArgument() ;
        recaptchaArguments.setIp(ip);
        recaptchaArguments.setRecaptchaResponse(recaptchaResponse);
//        String VerifyMessage =
//                (String)captchaService.verifyCaptcha(recaptchaResponse);
        log.info("capatcha verified responce");
        String captchaVerifyMessage =
                (String)captchaService.verifyCaptcha(recaptchaArguments);
        log.info("capatcha verified");
        if ( StringUtils.isNotEmpty(captchaVerifyMessage)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", captchaVerifyMessage);
        log.info("bad capatcha");
            return ResponseEntity.badRequest().build();
        }

        UserEntity data = dataMapper.map(userDto, UserEntity.class);
        UserEntity newUser = userRegistrationService.saveNewUserData(data);
        UserDTO createdUser = dataMapper.map(newUser, UserDTO.class);
        return ResponseEntity.ok().body(createdUser);
    }

    @Override
    public String verifyUser(String code) {
        String verificationString ;
        if (emailVerificationService.verify(code)) {

        verificationString = "<h3>Your E-mail has been verified.\n</h3>";
        return verificationString;

        } else {
             verificationString = " <h3>Something went wrong while verifying your E-mail, please contact support!\n</h3>";
            return verificationString ;
        }
    }

    @Override
    public List<UserDTO> findByAll() {
        List<UserEntity> contatList = userRegistrationService.getAll();
        List<UserDTO> mapList = contatList.stream().map(person -> dataMapper.map(person, UserDTO.class))
                .collect(Collectors.toList());
        return mapList;

    }


}
