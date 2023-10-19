package com.loginUser.registration;


import com.loginUser.event.RegistrationCompleteEvent;
import com.loginUser.event.listener.RegistrationCompleteEventListener;
import com.loginUser.registration.token.VerificationToken;
import com.loginUser.registration.token.VerificationTokenRepository;
import com.loginUser.user.User;
import com.loginUser.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

/**
 * @author Sampson Alfred
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository tokenRepository;
    private final RegistrationCompleteEventListener eventListener;
    private final HttpServletRequest servletRequest;
    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);


    @PostMapping
    public String registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request){
        User user = userService.registerUser(registrationRequest);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        return "Success!  Please, check your email for to complete your registration";
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token){
        String url = applicationUrl(servletRequest)+"/register/resend-verification-token?token="+token;
        VerificationToken theToken = tokenRepository.findByToken(token);
        if (theToken.getUser().isEnabled()){
            return "This account has already been verified, please, login.";
        }
        String verificationResult = userService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")){
            return "Email verified successfully. Now you can login to your account";
        }
        return "Invalid verification token, <a href=\"" +url+"\"> Get a new Varification Link. </a>";
    }


@GetMapping("/resend-verification-token")
  public String resendVarificationToken(@RequestParam("token") String oldToken, final HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {

        VerificationToken verificationToken = userService.generateNewVarificationToken(oldToken);
        User theUser = verificationToken.getUser();

        resendVarificationTokenEmail(theUser,applicationUrl(request), verificationToken);
        return "A new  verification Link has been sent to you email," +
                "please check to activate  your Account";

  }

    private void resendVarificationTokenEmail(User theUser, String applicationUrl,
                                              VerificationToken verificationToken) throws MessagingException, UnsupportedEncodingException {
        //4 Build the verification url to be sent to the user
        String url = applicationUrl+"/register/verifyEmail?token="+verificationToken.getToken();
        eventListener.sendVerificationEmail(url);

        log.info("Click the link to verify your registration :  {}", url);
    }


    public String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"
                +request.getServerPort()+request.getContextPath();
    }
}