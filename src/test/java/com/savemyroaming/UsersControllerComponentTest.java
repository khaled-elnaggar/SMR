package com.savemyroaming;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.savemyroaming.DTO.UserDTO;
import com.savemyroaming.capatcha.RecaptchaService;
import com.savemyroaming.repositories.UserRepository;
import lombok.SneakyThrows;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsersControllerComponentTest {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TestHelper testHelper;

  @ClassRule
  public static WireMockRule googleCaptchaServer = new WireMockRule(8000);

  @ClassRule
  public static GreenMail smtpServer;

  @BeforeAll
  static void beforeAll() {
    googleCaptchaServer.start();
    smtpServer = new GreenMail(new ServerSetup(587, null, "smtp"));
    smtpServer.setUser("savemyroaming123@gmail.com", "xylskzrvbjhsheds");
    smtpServer.start();
  }

  @AfterAll
  static void afterAll() {
    googleCaptchaServer.stop();
    smtpServer.stop();
  }

  @Test
  @SneakyThrows
  void whenCreateNewUserWithValidData_ThenSendMessageSuccessfully() {
    //Given
    UserDTO user = new UserDTO();
    user.setFirst_name("Ahmed");
    user.setLast_name("Mohammed");
    user.setEmail("myemail@mohammed.me");

    String recaptchaResponse = "hLyfxmzDYCThYTbrHF90";

    googleCaptchaServer.stubFor(
            WireMock.post(RecaptchaService.GOOGLE_RECAPTCHA_VERIFY_URL + "?secret=google-secret" +
                            "&response=" + recaptchaResponse + "&remoteip=127.0.0.1")
                    .willReturn(aResponse()
                            .withBody("{\"success\": true}")
                            .withStatus(200)
                            .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
    );

    //When
    MyResponse<Void> response = testHelper.registerUser(user, recaptchaResponse);

    //Then
    assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());

    MimeMessage receivedMail = smtpServer.getReceivedMessages()[0];
    assertThat(receivedMail.getSubject()).isEqualTo("Please verify your registration");
    assertThat(receivedMail.getAllRecipients()).hasSize(1).contains(new InternetAddress(user.getEmail()));

    assertNotNull(userRepository.findByEmail(user.getEmail()));
  }
}
