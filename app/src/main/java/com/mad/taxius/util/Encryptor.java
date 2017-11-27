package com.mad.taxius.util;

import android.util.Log;

import com.mad.taxius.constant.Constant;

import java.security.CodeSigner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class contains encryption methods
 */

public class Encryptor {

    private static MessageDigest mDigest;
    private static final String encryptionType = "MD5";

    /**
     * Encrypt plain text for security reason with MD5
     *
     * @param plainText
     * @return
     */
    public static String encrypt(String plainText) {
        if (plainText == null || plainText.isEmpty()) return "";
        try {
            mDigest = MessageDigest.getInstance(encryptionType);
            mDigest.reset();
            byte[] digested = mDigest.digest(plainText.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < digested.length; i++) {
                stringBuffer.append(Integer.toHexString(0xff & digested[i]));
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(Constant.Debug.TAG, "Error from Encryption of password");
            return null;
        }
    }
}
