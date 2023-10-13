package com.loginUser.event.listener;

import com.loginUser.event.RegistrationCompleteEvent;
import com.loginUser.user.User;
import com.loginUser.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    public final  UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // 1.Get the newly registered User
        User theUser = event.getUser();

        //2. Create a verification token for the user
        String verificationToken = UUID.randomUUID().toString();

        //3. save the verification token for the user
        userService.saveUserVerificationToken(theUser, verificationToken);

        //4. Build the verification url to be sent to the user
        String url = event.getApplicationUrl()+"/register/verifyEmail?token="+verificationToken;

        //5. Send the email
        log.info("Click the link to verify your registration: {}", url);
    }
}
