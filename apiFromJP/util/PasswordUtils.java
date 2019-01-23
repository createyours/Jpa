package org.leadingsoft.golf.api.util;

import org.leadingsoft.golf.api.service.CollectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PasswordUtils {
  private final static Logger logger = LoggerFactory.getLogger(CollectService.class);

  public static boolean isValidPassword(String rawPassword, String encoderPassword) {
    return getEncoderPassword(rawPassword).equals(encoderPassword);
  }

  public static String getEncoderPassword(String rawPassword) {
    try {
      return RandomUtils.getBCryptPasswordEncoderString(rawPassword);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return null;
    }
  }
}
