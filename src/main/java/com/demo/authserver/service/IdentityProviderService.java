package com.demo.authserver.service;

import com.demo.authserver.configuration.constants.ErrorMessage;
import com.demo.authserver.entity.IdentityProvider;
import com.demo.authserver.model.CustomException;
import com.demo.authserver.repository.IdentityProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IdentityProviderService {


    private IdentityProviderRepository identityProviderRepository;

    @Autowired
    public IdentityProviderService(IdentityProviderRepository identityProviderRepository) {
        this.identityProviderRepository = identityProviderRepository;
    }

    public void saveIdentityProvider(IdentityProvider identityProvider) {
        Optional<IdentityProvider> optionalIdentityProvider = identityProviderRepository.findByProvider(identityProvider.getProvider());
        if (optionalIdentityProvider.isPresent()) {
            throw new CustomException(ErrorMessage.IDENTITY_PROVIDER_EXIST.getMessage(), HttpStatus.CONFLICT);
        }
        identityProviderRepository.save(identityProvider);
    }

    public void updateIdentityProvider(IdentityProvider identityProvider, String beforeUpdateProviderName) {
        Optional<IdentityProvider> optionalIdentityProvider = identityProviderRepository.findByProvider(beforeUpdateProviderName);
        if (!optionalIdentityProvider.isPresent()) {
            throw new CustomException(ErrorMessage.IDENTITY_PROVIDER_NOT_FOUND.getMessage(),
                    HttpStatus.NOT_FOUND);
        }
        if (identityProvider.getProvider().isEmpty())
            throw new CustomException(ErrorMessage.IDENTITY_PROVIDER_NOT_NULL.getMessage(), HttpStatus.BAD_REQUEST);

        identityProviderRepository.save(identityProvider);
    }

    public IdentityProvider findByProvider(String provider) {
        final IdentityProvider identityProvider = identityProviderRepository.findByProvider(provider).orElseThrow(() ->
                new CustomException(ErrorMessage.IDENTITY_PROVIDER_NOT_FOUND.getMessage(),
                        HttpStatus.NOT_FOUND));

        return identityProvider;
    }
}
