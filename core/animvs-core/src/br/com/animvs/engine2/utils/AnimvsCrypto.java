package br.com.animvs.engine2.utils;

import com.badlogic.gdx.Gdx;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public final class AnimvsCrypto {

    public static String obfuscate(String str, String key) {
        StringBuffer sb = new StringBuffer(str);

        int lenStr = str.length();
        int lenKey = key.length();

        //
        // For each character in our string, encrypt it...
        for (int i = 0, j = 0; i < lenStr; i++, j++) {
            if (j >= lenKey)
                j = 0; // Wrap 'round to beginning of key string.

            //
            // XOR the chars together. Must cast back to char to avoid compile
            // error.
            //
            sb.setCharAt(i, (char) (str.charAt(i) ^ key.charAt(j)));
        }

        return sb.toString();
    }

    public static String deobfuscate(String str, String key) {
        //
        // To 'decryptAES' the string, simply apply the same technique.
        return obfuscate(str, key);
    }

    public static void testObfuscation(String key, String text) {
        try {
            String encryptedStr = new String(obfuscate(text, key));
            String decryptedStr = deobfuscate(encryptedStr, key);
            Gdx.app.log("CRYPT TEST", "Encrypted message: " + encryptedStr);
            Gdx.app.log("CRYPT TEST", "Decrypted message: " + decryptedStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testAES(String seed, String clearText) {
        try {
            String encryptedStr = encryptAES(seed, clearText);
            String decryptedStr = decryptAES(seed, encryptedStr);
            Gdx.app.log("CRYPT TEST", "Encrypted message: " + encryptedStr);
            Gdx.app.log("CRYPT TEST", "Decrypted message: " + decryptedStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String encryptAES(String seed, String cleartext) throws Exception {
        byte[] rawKey = getRawKey(seed.getBytes());
        byte[] result = encrypt(rawKey, cleartext.getBytes());
        return toHex(result);
    }

    public static String decryptAES(String seed, String encrypted) throws Exception {
        byte[] rawKey = getRawKey(seed.getBytes());
        byte[] enc = toByte(encrypted);
        byte[] result = decrypt(rawKey, enc);
        return new String(result);
    }

    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        sr.setSeed(seed);
        kgen.init(128, sr); // 192 and 256 bits may not be available
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }


    public static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    public static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private final static String HEX = "0123456789ABCDEF";

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }
}
