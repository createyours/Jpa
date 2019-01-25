package org.leadingsoft.golf.api.code;

/**
 * 性別コード
 */
public enum SexCode {
  /** 男 */
  BOY("1", "男"),
  /** 女 */
  GIRL("0", "女"),;

  private String code_;
  private String codeNm_;

  SexCode(String code, String codeNm) {
    this.code_ = code;
    this.codeNm_ = codeNm;
  }

  public String code() {
    return code_;
  }

  public String codeNm() {
    return codeNm_;
  }
}
