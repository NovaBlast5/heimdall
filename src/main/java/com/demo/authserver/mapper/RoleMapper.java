package com.demo.authserver.mapper;

import com.demo.authserver.dto.RoleDto;
import com.demo.authserver.entity.Role;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",uses = {RealmMapper.class,PrivilegeMapper.class},injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface RoleMapper {

    Role toRoleDao(RoleDto roleDto);
    RoleDto toRoleDto(Role role);
    List<RoleDto> toRoleDtoList(List<Role> list);
}
