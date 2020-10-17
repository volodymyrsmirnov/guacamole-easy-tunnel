package org.mindcollapse;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

final class EasyTunnelPayloadReader {
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM = "AES";
    private static final String DIGEST = "MD5";

    private static byte[] getIv(final String iv) throws NoSuchAlgorithmException {
        return Arrays.copyOfRange(MessageDigest.getInstance(DIGEST).digest(iv.getBytes()), 0, 16);
    }

    private static byte[] getKey(final String password) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(DIGEST).digest(password.getBytes());
    }

    static Map<String, String> read(final String payload)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        final Gson gson = new Gson();
        final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        final Base64.Decoder decoder = Base64.getDecoder();

        cipher.init(Cipher.DECRYPT_MODE,
                new SecretKeySpec(getKey(System.getenv("PAYLOAD_AES_KEY")), ALGORITHM),
                new IvParameterSpec(getIv(System.getenv("PAYLOAD_AES_IV"))));

        return gson.fromJson(
                new String(cipher.doFinal(decoder.decode(payload)), StandardCharsets.UTF_8),
                new TypeToken<Map<String, String>>(){}.getType());
    }
}
