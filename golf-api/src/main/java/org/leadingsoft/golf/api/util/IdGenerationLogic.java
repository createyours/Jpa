package org.leadingsoft.golf.api.util;

public class IdGenerationLogic {
  public static String getId() throws Exception {
    return getId(20);
  }

  public static String getId(int length) throws Exception {
    return RandomUtils.getMD5String(RandomUtils.getUUIDString()).substring(0, length);
  }
}
