package org.leadingsoft.golf.api.util;

public class FileNameUtils {
  /**
   * ファイルのsuffix
   * 
   * @param fileName
   * @return
   */
  public static String getSuffix(String fileName) {
    return fileName.substring(fileName.lastIndexOf("."));
  }

  /**
   * ファイルの名前
   * 
   * @param fileOriginName 源文件名
   * @return
   */
  public static String getFileName(String fileOriginName) {
    return RandomUtils.getUUIDString() + FileNameUtils.getSuffix(fileOriginName);
  }
}
