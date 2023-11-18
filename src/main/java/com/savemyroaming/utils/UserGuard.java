package com.savemyroaming.utils;

import com.savemyroaming.capatcha.CaptchaService;
import com.savemyroaming.capatcha.RecaptchaArgument;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class UserGuard {

  @Autowired
  private CaptchaService captchaService;

  public void validateRecaptcha(HttpServletRequest request, String recaptchaResponse) {
    String ip = request.getRemoteAddr();
    RecaptchaArgument recaptchaArguments = new RecaptchaArgument();
    recaptchaArguments.setIp(ip);
    recaptchaArguments.setRecaptchaResponse(recaptchaResponse);
    String captchaVerifyMessage =
            (String) captchaService.verifyCaptcha(recaptchaArguments);

    if (StringUtils.isNotEmpty(captchaVerifyMessage)) {
      throw new RuntimeException(captchaVerifyMessage);
    }
  }
}
