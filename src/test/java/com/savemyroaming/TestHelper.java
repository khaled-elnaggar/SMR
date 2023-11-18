package com.savemyroaming;

import com.savemyroaming.DTO.UserDTO;
import org.springframework.stereotype.Component;

@Component
public interface TestHelper {
  MyResponse<Void> registerUser(UserDTO user, String recaptchaResponse);
}
