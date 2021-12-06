package com.demo.authserver.mapper;

import com.demo.authserver.dto.GroupDto;
import com.demo.authserver.entity.UserGroup;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface GroupMapper {

    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    GroupDto toGroupDto(UserGroup userGroup);
    UserGroup toGroupDao(GroupDto groupDto);
    List<GroupDto> toGroupDtoList(List<UserGroup> list);

}
