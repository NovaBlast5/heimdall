package com.demo.authserver.mapper;
import com.demo.authserver.dto.PrivilegeDto;
import com.demo.authserver.entity.Privilege;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;
@Mapper(componentModel = "spring",uses = {RoleMapper.class},injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PrivilegeMapper {

    PrivilegeDto toPrivilegeDto(Privilege privilege);
    List<PrivilegeDto> toPrivilegeDtoList(List<Privilege> privileges);
    Set<PrivilegeDto> toPrivilegeDtoSet(Set<Privilege> privilegeSet);
}
