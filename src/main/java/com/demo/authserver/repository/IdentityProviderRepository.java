package com.demo.authserver.repository;

import com.demo.authserver.entity.IdentityProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdentityProviderRepository extends JpaRepository<IdentityProvider, Long> {

    Optional<IdentityProvider> findByProvider(String provider);
}
