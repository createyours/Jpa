package org.leadingsoft.golf.api.code;

/**
 * ログインコード
 */
public enum ResultCode {
  /** ログイン成功 */
  OK("0"),
  /** ログイン失敗 */
  NG("1");

  private String code_;

  ResultCode(String code) {
    this.code_ = code;
  }

  public String code() {
    return code_;
  }
}
