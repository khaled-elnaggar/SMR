package com.savemyroaming.capatcha;


public class MockCaptchaService implements CaptchaService<String,String>{

    @Override
    public String verifyCaptcha(String capatchaArgumentObject) {
        return capatchaArgumentObject;
    }
}
