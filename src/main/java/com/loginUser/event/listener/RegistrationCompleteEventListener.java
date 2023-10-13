package com.loginUser.event.listener;

import com.loginUser.event.RegistrationCompleteEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // 1.Get the newly register User
        //2. Create a verification token for the user
        //3. save the verification token for the user
        //4. Build the verification url to be sent to the user
        //5. Send the email
    }
}
