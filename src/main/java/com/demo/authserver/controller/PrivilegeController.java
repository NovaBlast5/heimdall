package com.demo.authserver.controller;

import com.demo.authserver.dto.PrivilegeDto;
import com.demo.authserver.dto.RoleDto;
import com.demo.authserver.model.ResponseMessage;
import com.demo.authserver.service.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;
@RestController
@RequestMapping("api/privilege")
public class PrivilegeController {

    private final PrivilegeService privilegeService;

    @Autowired
    public PrivilegeController(PrivilegeService privilegeService) {
        this.privilegeService = privilegeService;
    }

    @GetMapping
    public ResponseEntity<List<PrivilegeDto>> getAllPrivileges(){
        return ResponseEntity.ok().body(privilegeService.getAllPrivileges());
    }

    @GetMapping("/{realmName}/{roleName}/{resourceName}")
    public ResponseEntity<Set<PrivilegeDto>> getPrivilegesForResource(@PathVariable String realmName,@PathVariable String roleName,@PathVariable String resourceName){
        return ResponseEntity.ok().body(privilegeService.getPrivilegesForResource(realmName,roleName,resourceName));
    }

    @PutMapping("/{resourceName}/{privilegeName}/add")
    public ResponseEntity<ResponseMessage> addPrivilegeToRole(@PathVariable String privilegeName, @PathVariable String resourceName, @RequestBody RoleDto role){
        privilegeService.addPrivilegeToResourceForRole(privilegeName, resourceName, role);
        ResponseMessage responseMessage = new ResponseMessage("Privilege added to role successfully!");
        return ResponseEntity.ok().body(responseMessage);
    }

    @PutMapping("/{resourceName}/{privilegeName}/remove")
    public ResponseEntity<ResponseMessage> removePrivilegeFromRole(@PathVariable String privilegeName, @PathVariable String resourceName, @RequestBody RoleDto role){
        privilegeService.removePrivilegeFromRole(privilegeName, resourceName, role);
        ResponseMessage responseMessage = new ResponseMessage("Privilege removed from role successfully!");
        return ResponseEntity.ok().body(responseMessage);
    }
}
