package com.SaveMyRoaming.savemyroaming.services;

import com.SaveMyRoaming.savemyroaming.entities.UserEntity;
import com.SaveMyRoaming.savemyroaming.repositories.UserRepository;
import com.SaveMyRoaming.savemyroaming.utils.DateConverter;
import com.SaveMyRoaming.savemyroaming.utils.Constants;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.mail.MessagingException;
import javax.management.RuntimeErrorException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
        emailVerificationService.sendVerificationEmail(data, siteURL);
        return newUser;
    }

    @Override
    public List<UserEntity> getAll() {
        return userRepo.findAll();
    }

    private void setDataForEmailOrThrowIfInvalid (UserEntity data) throws Exception {
        boolean isValidEmail = isValidateUsername(data.getEmail());
        if (!isValidEmail)
            throw new Exception(Constants.MSG_INVALID_EMAIL);
        setDataForValidEmail(data);
    }

    private void setDataForValidEmail(UserEntity data) {
        UserEntity userEmail = userRepo.findByEmail(data.getEmail());
        checkIfUniqueEmailOrThrow(data, userEmail);
        data.setTimezone(getDate());
        String randomCode = RandomString.make(4);
        data.setVerificationCode(randomCode);
    }

    public void userDataNullOrEmptyValidation (UserEntity data){
        throwIfStringIsEmptyOrNull(data.getFirst_name(), Constants.MSG_ERROR_EMPTY_USER_FIRSTNAME);
        throwIfStringIsEmptyOrNull(data.getLast_name(), Constants.MSG_ERROR_EMPTY_USER_LASTNAME);
        throwIfStringIsEmptyOrNull(data.getEmail(), Constants.MSG_ERROR_EMPTY_USEREmail);
    }

    private void throwIfStringIsEmptyOrNull(String str, String error) {
        if (str == null || str.isBlank())
            throw new RuntimeErrorException(null, error);
    }


    public boolean  isValidateUsername (String email){
        String regex = Constants.EMAIL_REGEX;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return  matcher.matches();
    }

    private void checkIfUniqueEmailOrThrow(UserEntity data, UserEntity userEmail) {
        if (userEmail != null && data.getEmail().equals(userEmail.getEmail()))
                throw new RuntimeErrorException(null, Constants.MSG_ERROR_EMPTY_USEREMAIL);
    }

    private String getDate() {
        DateTime now = DateTime.now(DateTimeZone.UTC);
        String intervalDate = DateConverter.formatDate(now.toDate(), DateTimeZone.UTC);
        return intervalDate;
    }

}
