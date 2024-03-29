package com.savemyroaming.capatcha;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RecaptchaService implements CaptchaService<RecaptchaArgument, String> {

  @Value("${google.recaptcha.key.secret}")
  String recaptchaSecret;
  @Value("${google.recaptcha.host}")
  String googleHost;

  public static final String GOOGLE_RECAPTCHA_VERIFY_URL =
          "/recaptcha/api/siteverify";
  @Autowired
  RestTemplateBuilder restTemplateBuilder;

  private String verifyRecaptcha(String ip, String recaptchaResponse) {
    Map<String, String> body = new HashMap<>();
    body.put("secret", recaptchaSecret);
    body.put("response", recaptchaResponse);
    body.put("remoteip", ip);
    log.debug("Request body for recaptcha: {}", body);
    ResponseEntity<Map> recaptchaResponseEntity =
            restTemplateBuilder.build()
                    .postForEntity(googleHost + GOOGLE_RECAPTCHA_VERIFY_URL +
                                    "?secret={secret}&response={response}&remoteip={remoteip}",
                            body, Map.class, body);
    log.debug("Response from recaptcha: {}", recaptchaResponseEntity);
    Map<String, Object> responseBody = recaptchaResponseEntity.getBody();
    boolean recaptchaSucess = (Boolean) responseBody.get("success");
    if (!recaptchaSucess) {
      List<String> errorCodes = (List) responseBody.get("error-codes");
      String errorMessage = errorCodes.stream()
              .map(s -> RecaptchaUtil.RECAPTCHA_ERROR_CODE.get(s))
              .collect(Collectors.joining(", "));
      return errorMessage;
    } else {
      return StringUtils.EMPTY;
    }
  }

  @Override
  public String verifyCaptcha(RecaptchaArgument recaptchaArguments) {

    return verifyRecaptcha(recaptchaArguments.getIp(), recaptchaArguments.getRecaptchaResponse());
  }
}