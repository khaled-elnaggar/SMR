package com.SaveMyRoaming.savemyroaming.capatcha;

import org.springframework.stereotype.Service;


public interface CaptchaService <T,Y>{

     Y verifyCaptcha( T capatchaArgumentObject);

}
