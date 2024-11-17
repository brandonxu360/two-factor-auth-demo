package com.xu.backend.repository;

import com.warrenstrange.googleauth.ICredentialRepository;
import com.xu.backend.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class TotpCredentialRepository implements ICredentialRepository {

    private final Map<String, String> usersKeys = new ConcurrentHashMap<>();

    // This method should return the secret key stored in the hash map.
    @Override
    public String getSecretKey(String id) {
        return usersKeys.get(id);
    }

    // This method should save the secret key, validation code, and scratch codes.
    // NOTE: We are only saving the secret key for the purpose of this demo, saving the validation code and scratch codes
    // could be implemented in a real application.
    @Override
    public void saveUserCredentials(String id, String secretKey, int validationCode, List<Integer> scratchCodes) {
        usersKeys.put(id, secretKey);
    }
}
