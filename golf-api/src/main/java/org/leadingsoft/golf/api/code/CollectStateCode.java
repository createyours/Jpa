package org.leadingsoft.golf.api.code;

/**
 * 募集情報の状態
 * 
 * <pre>
 * 
 * </pre>
 */
public enum CollectStateCode {
  INIT("0"), COMPLETE("1"), DELETE("2");

  private String code_;

  CollectStateCode(String code) {
    code_ = code;
  }

  public String code() {
    return code_;
  }
}
