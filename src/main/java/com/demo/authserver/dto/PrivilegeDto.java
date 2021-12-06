package com.demo.authserver.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrivilegeDto {

    private String name;
    public PrivilegeDto() {
    }
    public PrivilegeDto(String name) {
        this.name = name;
    }
}
