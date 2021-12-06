package com.demo.authserver.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.demo.authserver.entity.Realm;
import com.demo.authserver.entity.IdentityProvider;
import com.demo.authserver.entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUserDto implements Serializable {

    private Long id;
    private String username;
    private String password;
    private String token;
    private String refreshToken;
    private String code;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Set<Role> roles = new HashSet<>();
    private String email;
    private Boolean isActivated;
    private String emailCode;
    private Realm realm;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private IdentityProvider identityProvider;
    public AppUserDto() {
    }

    public AppUserDto(String username, String password, String code, String token, Set<Role> roles, String email,
                      Boolean isActivated, String emailCode) {
        this.username = username;
        this.password = password;
        this.code = code;
        this.token = token;
        this.roles = roles;
        this.email = email;
        this.isActivated = isActivated;
        this.emailCode = emailCode;
    }
}
