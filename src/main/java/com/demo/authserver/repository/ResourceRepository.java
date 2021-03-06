package com.demo.authserver.repository;
import com.demo.authserver.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
@Repository
public interface ResourceRepository extends JpaRepository<Resource,Long> {
    Optional<Resource> findByName(String name);
    @Query("SELECT rr from Role r left join r.roleResources rr where r.name=?1 and r.realm.name=?2")
    Set<Resource> getResourcesForRole(String roleName,String realmName);
}
