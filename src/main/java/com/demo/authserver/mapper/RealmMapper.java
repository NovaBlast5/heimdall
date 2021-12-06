package com.demo.authserver.mapper;

import com.demo.authserver.dto.RealmDto;
import com.demo.authserver.entity.Realm;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface RealmMapper {

    Realm toRealmDao(RealmDto realmDto);
    RealmDto toRealmDto(Realm realm);
    List<RealmDto> toRealmDtoList(List<Realm> list);
}
