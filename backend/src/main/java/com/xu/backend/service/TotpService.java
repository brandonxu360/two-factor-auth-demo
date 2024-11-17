package com.xu.backend.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import com.xu.backend.repository.UserRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class TotpService {
    private final GoogleAuthenticator googleAuthenticator;
    private final UserRepository userRepository;

    /**
     * Get the TOTP QR code for the user with the given ID
     * @param id The user ID
     * @param response The HTTP response to write the QR code image to
     * @throws WriterException If an error occurs while generating the QR code
     * @throws IOException If an error occurs while writing the QR code to the response
     */
    public void getTotpQRSetup(String id, HttpServletResponse response) throws WriterException, IOException {

        try {
            // Generate a new secret key for the user
            final GoogleAuthenticatorKey key = googleAuthenticator.createCredentials(id);

            // Generate the QR code URL from the secret key
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            String qrUrl = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("MyApp", id, key);

            // Generate the QR code image from the URL
            BitMatrix bitMatrix = qrCodeWriter.encode(qrUrl, BarcodeFormat.QR_CODE, 200, 200);

            // Write the QR code image to the response
            response.setContentType("image/png");
            ServletOutputStream outputStream = response.getOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            outputStream.close();
        } catch (WriterException | IOException e) {
            // If an error occurs, send an internal server error response
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generating QR code");
            throw e;
        }
    }

    public boolean verifyTotpSetup(String id, int code) {
        return googleAuthenticator.authorizeUser(id, code);
    }

    public void saveTotpSecret(String id) {
        String secret = googleAuthenticator.getCredentialRepository().getSecretKey(id);
        userRepository.saveTotpSecretById(id, secret);
    }

    public void updateTwoFactorStatus(String id, boolean status) {
        userRepository.updateTwoFactorStatus(id, status);
    }
}
