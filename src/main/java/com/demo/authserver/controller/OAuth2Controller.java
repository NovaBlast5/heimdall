package com.demo.authserver.controller;

import com.demo.authserver.model.Code;
import com.demo.authserver.model.ResponseMessage;
import com.demo.authserver.model.oauth.OAuthSocialUser;
import com.demo.authserver.service.OAuth2SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("oauth")
public class OAuth2Controller {

    @Autowired
    private OAuth2SocialService oAuth2SocialService;

    @PostMapping(path = "/social-login")
    public ResponseEntity<?> socialLogin(@RequestBody OAuthSocialUser oAuthSocialUser) {

        Code code = oAuth2SocialService.getCode(oAuthSocialUser);
        if (code != null) {
            return ResponseEntity.ok().body(code);
        }
        final ResponseMessage responseMessage = new ResponseMessage("Failed to generate JWT Code");
        return ResponseEntity.unprocessableEntity().body(responseMessage);
    }

}
