package com.savemyroaming;

import com.savemyroaming.DTO.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class RestTestHelper implements TestHelper {

  @Autowired
  private TestRestTemplate restTemplate;

  public RestTestHelper(TestRestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public MyHttpResponse<Void> registerUser(UserDTO user, String recaptchaResponse) {

    HttpEntity<UserDTO> request = new HttpEntity<>(user);
    ResponseEntity<Void> springResponse = restTemplate.exchange("/user/register?recaptchaResponse=" + recaptchaResponse,
            HttpMethod.POST, request, Void.class);

    return new MyHttpResponse<>(springResponse);
  }
}
