package com.xu.backend.controller;

import com.google.zxing.WriterException;
import com.xu.backend.service.TotpService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TotpController {
    private final TotpService totpService;

    // Write the QR code image for TOTP setup to the response
    @GetMapping("/totp-setup")
    public void getTotpSetup(@AuthenticationPrincipal OAuth2User principal, HttpServletResponse response) throws IOException, WriterException {
        String id = principal.getName();
        totpService.getTotpQRSetup(id, response);
    }

    // Verify the TOTP code for the user with the given ID
    @PostMapping("/totp-verify")
    public boolean verifyTotp(@AuthenticationPrincipal OAuth2User principal, @RequestBody Map<String, String> requestBody, HttpServletRequest request) {
        String id = principal.getName();
        int code = Integer.parseInt(requestBody.get("code"));
        boolean verified =  totpService.verifyTotpSetup(id, code);

        if (verified) {
            // If the TOTP code is verified, save the secret enable two-factor authentication for the user
            totpService.saveTotpSecret(id);
            totpService.updateTwoFactorStatus(id, true);

            // Set the current session as two-factor authenticated
            request.getSession().setAttribute("isTwoFactorAuthenticated", true);
        }

        return verified;
    }
}
