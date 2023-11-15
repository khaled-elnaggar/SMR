package com.savemyroaming.capatcha;


public interface CaptchaService<T, Y> {

  Y verifyCaptcha(T capatchaArgumentObject);

}
