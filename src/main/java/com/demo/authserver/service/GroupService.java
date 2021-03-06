package com.demo.authserver.service;

import com.demo.authserver.dto.AppUserDto;
import com.demo.authserver.dto.GroupDto;
import com.demo.authserver.entity.AppUser;
import com.demo.authserver.entity.Role;
import com.demo.authserver.entity.UserGroup;
import com.demo.authserver.mapper.AppUserMapper;
import com.demo.authserver.mapper.GroupMapper;
import com.demo.authserver.mapper.GroupMapperClass;
import com.demo.authserver.model.CustomException;
import com.demo.authserver.repository.AppUserRepository;
import com.demo.authserver.repository.GroupRepository;
import com.demo.authserver.repository.RealmRepository;
import com.demo.authserver.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GroupService {
    private GroupRepository groupRepository;
    private GroupMapper groupMapper;
    private GroupMapperClass groupMapperClass;
    private AppUserRepository appUserRepository;
    private UserService userService;
    private AppUserMapper appUserMapper;
    private RealmRepository realmRepository;
    private RoleRepository roleRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository, GroupMapper groupMapper, GroupMapperClass groupMapperClass, AppUserRepository appUserRepository, UserService userService, AppUserMapper appUserMapper, RealmRepository realmRepository, RoleRepository roleRepository) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
        this.groupMapperClass = groupMapperClass;
        this.appUserRepository = appUserRepository;
        this.userService = userService;
        this.appUserMapper = appUserMapper;
        this.realmRepository = realmRepository;
        this.roleRepository = roleRepository;
    }

    public List<GroupDto> findAllGroups(String realmName) {
        return groupMapperClass.daoListToDto(groupRepository.findAllByRealmName(realmName));
    }

    public void createGroup(String realmName, UserGroup userGroup) {
        Optional<UserGroup> byGroupName = groupRepository.findByNameAndRealmName(userGroup.getName(), realmName);

        if (byGroupName.isPresent())
            throw new CustomException(
                    "Group with the name [ " + byGroupName.get().getName() + " ] already exists!",
                    HttpStatus.CONFLICT);
        else if (userGroup.getName().equals("") || userGroup.getName().matches("^\\s*$")){
            throw new CustomException("The inserted group cannot be null!", HttpStatus.BAD_REQUEST);
        } else {
            userGroup.setRealm(realmRepository.findByName(realmName).get());
            groupRepository.save(userGroup);
        }
    }

    public void deleteGroupByName(String realmName,String name) {
        if (groupRepository.findByNameAndRealmName(name,realmName).isPresent()) {
            groupRepository.deleteByName(name);
        } else {
            throw new CustomException(
                    "Group with name [ " + name + " ] does not exist", HttpStatus.BAD_REQUEST
            );
        }
    }

    public GroupDto findGroupByName(String name, String realmName) {
        UserGroup byGroupName = groupRepository.findByNameAndRealmName(name, realmName).orElseThrow(() -> new CustomException("Group with the name [ " + name + " ] does not exists!",HttpStatus.NOT_FOUND));
        return groupMapperClass.daoToDto(byGroupName);
    }

    public void updateByName(String realmName,String name, GroupDto group) {
       UserGroup oldGroup = groupRepository.findByNameAndRealmName(name,realmName).orElseThrow(() -> new CustomException("Group with name " + name + " not found", HttpStatus.BAD_REQUEST));
       UserGroup newGroup = groupMapperClass.dtoToDao(group);
        if (!group.getName().isEmpty() || !group.getName().equals(" ")) {
            oldGroup.setName(group.getName());
        }
        if (!group.getUsers().isEmpty() || group.getUsers().size() > 0) {
            oldGroup.setAppUserGroup(newGroup.getAppUserGroup());
        }
       groupRepository.save(oldGroup);
    }


    public void addRoleForGroup(String realmName,String name, String roleName) {
        Role role = roleRepository.findByNameAndRealmName(roleName, realmName).orElseThrow(() -> new CustomException("Role with the name " + roleName + " could not be found!", HttpStatus.NOT_FOUND));
        Optional<UserGroup> group = groupRepository.findByNameAndRealmName(name,realmName);
        if (group.isPresent()) {
            List<AppUser> users = group.get().getAppUserGroup();
            for (AppUser user : users) {
                if (!user.getRoles().contains(role)) {
                    user.getRoles().add(role);
                    appUserRepository.save(user);
                }
            }
        }
        else{
            throw new CustomException("Group with the name "+ name +" could not be found!",HttpStatus.NOT_FOUND);
        }
    }

    public void addUserToGroup(String realmName,String name, AppUser user) {
        Optional<UserGroup> group = groupRepository.findByNameAndRealmName(name,realmName);
        group.ifPresent(g -> g.getAppUserGroup().add(user));
        groupRepository.save(group.get());

    }

    public List<AppUserDto> getUsersFromGroup(String realmName, String groupName) {
        UserGroup userGroup = groupRepository.findByNameAndRealmName(groupName,realmName).orElseThrow(() -> new CustomException("Group with name" + groupName + " not found", HttpStatus.BAD_REQUEST));
        List<AppUser> appUsers = userGroup.getAppUserGroup();
        return appUserMapper.toAppUserDtoList(appUsers);
    }

    public void deleteUserFromGroupByName(String realmName,String groupName, String username) {
        UserGroup userGroup = groupRepository
                .findByNameAndRealmName(groupName,realmName)
                .orElseThrow(() -> new CustomException("Group with name" + groupName + " not found", HttpStatus.BAD_REQUEST));

        userGroup.getAppUserGroup().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .ifPresent(u -> userGroup.getAppUserGroup().remove(u));
        groupRepository.save(userGroup);
    }

}
