package com.demo.authserver.dto;

import com.demo.authserver.entity.Realm;
import lombok.*;

import java.util.List;
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto {
    private String name;
    private List<AppUserDto> users;
    private Realm realm;

}
