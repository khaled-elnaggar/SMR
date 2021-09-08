package com.SaveMyRoaming.savemyroaming.services;

import com.SaveMyRoaming.savemyroaming.entities.UserEntity;
import com.SaveMyRoaming.savemyroaming.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;


@Service
public class EmailVerificationServiceImpl implements EmailVerificationService{

    @Autowired
    private UserRepository repo;


    @Override
    public boolean verify(String verificationCode) {
        UserEntity user = repo.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled() ) {
            return false;
        }
        else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            repo.save(user);
            return true;
        }
    }
}
