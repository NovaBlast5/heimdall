package com.demo.authserver.repository;

import com.demo.authserver.entity.Realm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RealmRepository extends JpaRepository<Realm, Long> {
    Optional<Realm> findByName(String name);
}
