package com.demo.authserver.repository;
import com.demo.authserver.entity.Resource;
import com.demo.authserver.entity.Role;
import com.demo.authserver.entity.RoleResourcePrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleResourcePrivilegeRepository extends JpaRepository<RoleResourcePrivilege,Long> {
    Optional<RoleResourcePrivilege> findByRoleAndResource(Role role, Resource resource);
}
