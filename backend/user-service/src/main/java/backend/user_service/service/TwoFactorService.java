package backend.user_service.service;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.time.Instant;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Service
public class TwoFactorService {
    private static final int SECRET_BYTES = 20;
    private static final int CODE_DIGITS = 6;
    private static final int TIME_STEP_SECONDS = 30;
    private static final int ALLOWED_WINDOW_STEPS = 1;

    private final SecureRandom secureRandom = new SecureRandom();
    private final Base32 base32 = new Base32();

    public String generateSecret() {
        byte[] randomBytes = new byte[SECRET_BYTES];
        secureRandom.nextBytes(randomBytes);
        return base32.encodeToString(randomBytes).replace("=", "");
    }

    public boolean verifyCode(String secret, String code) {
        if (secret == null || secret.isBlank() || code == null || !code.matches("\\d{6}")) {
            return false;
        }

        long currentCounter = Instant.now().getEpochSecond() / TIME_STEP_SECONDS;

        for (int i = -ALLOWED_WINDOW_STEPS; i <= ALLOWED_WINDOW_STEPS; i++) {
            String candidate = generateTotp(secret, currentCounter + i);
            if (candidate.equals(code)) {
                return true;
            }
        }

        return false;
    }

    public String buildOtpAuthUrl(String email, String secret) {
        String issuer = "MovieRecommender";
        return "otpauth://totp/" + issuer + ":" + email + "?secret=" + secret + "&issuer=" + issuer + "&algorithm=SHA1&digits=6&period=30";
    }

    public byte[] generateQrCode(String otpAuthUrl) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix matrix = qrCodeWriter.encode(otpAuthUrl, BarcodeFormat.QR_CODE, 250, 250);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", pngOutputStream);

        return pngOutputStream.toByteArray();
    }

    public String generateTotp(String base32Secret, long counter) {
        try {
            byte[] key = base32.decode(base32Secret);
            byte[] counterBytes = ByteBuffer.allocate(8).putLong(counter).array();

            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(key, "HmacSHA1"));

            byte[] hash = mac.doFinal(counterBytes);
            int offset = hash[hash.length - 1] & 0x0F;
            int binary = ((hash[offset] & 0x7F) << 24)
                | ((hash[offset + 1] & 0xFF) << 16)
                | ((hash[offset + 2] & 0xFF) << 8)
                | (hash[offset + 3] & 0xFF);

            int otp = binary % (int) Math.pow(10, CODE_DIGITS);
            return String.format("%0" + CODE_DIGITS + "d", otp);
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("Unable to generate TOTP", ex);
        }
    }
}
