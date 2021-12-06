package com.demo.authserver.mapper;

import com.demo.authserver.dto.AppUserDto;
import com.demo.authserver.entity.AppUser;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RoleMapper.class, RealmMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)

public interface AppUserMapper {


    AppUser toAppUserDao(AppUserDto appUserDto);

    AppUserDto toAppUserDto(AppUser appUser);

    List<AppUserDto> toAppUserDtoList(List<AppUser> list);

    List<AppUser> toAppUserDaoList(List<AppUserDto> list);
}
