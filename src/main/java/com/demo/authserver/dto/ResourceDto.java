package com.demo.authserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceDto {

    private String name;

    public ResourceDto() {
    }

    public ResourceDto(String name) {
        this.name = name;
    }
}
