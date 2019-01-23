package org.leadingsoft.golf.api.util;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class RandomUtils {

  public static String getUUIDString() {
    UUID uuid = UUID.randomUUID();
    String stredUuid = uuid.toString().toLowerCase().replace("-", "");
    return stredUuid;
  }

  public static String getMD5String(String seed) throws Exception {
    try {
      String hashed = getDigestString(seed, "MD5");
      return hashed;
    } catch (NoSuchAlgorithmException e) {
      throw e;
    }
  }

  public static String getBCryptPasswordEncoderString(String seed) throws Exception {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String pasWd = encoder.encode(seed);
    return pasWd;
  }

  public static int makeAuthCode() {
    int authCodeNew = 0;
    authCodeNew = (int) Math.round(Math.random() * (9999 - 1000) + 1000);
    return authCodeNew;
  }

  public static String getDigestString(String seed, String algorithm) throws Exception {
    MessageDigest md = MessageDigest.getInstance(algorithm);
    byte[] bs = seed.getBytes(Charset.forName("US-ASCII"));
    byte[] hashed = md.digest(bs);
    return byteToHexString(hashed);
  }

  public static String byteToHexString(byte[] bs) {
    StringBuilder sb = new StringBuilder(bs.length * 2);
    for (byte b : bs) {
      sb.append(Character.forDigit((b & 0xf0) >> 4, 16));
      sb.append(Character.forDigit((b & 0x0f), 16));
    }
    return sb.toString();
  }

  public static String getRandomNumber(int len) {
    SecureRandom sr = new SecureRandom();
    String result = (sr.nextInt(9) + 1) + "";
    for (int i = 0; i < len - 2; i++)
      result += sr.nextInt(10);
    result += (sr.nextInt(9) + 1);
    return result;
  }
}
