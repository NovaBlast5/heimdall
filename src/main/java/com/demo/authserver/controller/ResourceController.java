package com.demo.authserver.controller;
import com.demo.authserver.dto.ResourceDto;
import com.demo.authserver.model.ResponseMessage;
import com.demo.authserver.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
@RestController
@RequestMapping("api/resources")
public class ResourceController {

    private final ResourceService resourceService;
    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ResourceDto>> getAllAvailableResources(){
        return ResponseEntity.ok().body(resourceService.getAllResourcesFromDb());
    }
    @GetMapping("/{realmName}/{roleName}")
    public ResponseEntity<Set<ResourceDto>> getAssignedResourcesForRole(@PathVariable String realmName, @PathVariable String roleName){
        return ResponseEntity.ok().body(resourceService.getAllResourcesForRole(realmName,roleName));
    }
    @PutMapping("/{resourceName}")
    public ResponseEntity<ResponseMessage> updateResourceByName(@PathVariable String resourceName, @RequestBody String newName){
        resourceService.updateResourceByName(resourceName,newName);
        ResponseMessage responseMessage = new ResponseMessage("Resource updated successfully!");
        return ResponseEntity.ok().body(responseMessage);
    }
    @PostMapping
    public ResponseEntity<ResponseMessage> createResource(@RequestBody ResourceDto resourceDto){
        resourceService.addResourceToDb(resourceDto);
        ResponseMessage responseMessage = new ResponseMessage("Resource created successfully!");
        return ResponseEntity.ok().body(responseMessage);
    }
    @DeleteMapping("/{resourceName}/removeAll")
    public ResponseEntity<ResponseMessage> removeResourceFromDatabase(@PathVariable String resourceName){
        resourceService.removeResourceFromDb(resourceName);
        ResponseMessage responseMessage = new ResponseMessage("Resource deleted successfully!");
        return ResponseEntity.ok().body(responseMessage);
    }

}
