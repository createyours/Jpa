package org.leadingsoft.golf.api.code;

/**
 * 承認ステータス
 */
public enum ApovStatusCode {
  /** 未承認 */
  UNApov("0", "未承認"),
  /** 承認済み */
  ApovOK("1", "承認済"),
  /** 却下する */
  ApovNG("2", "却下する"),;

  private String code_;
  private String codeNm_;

  ApovStatusCode(String code, String codeNm) {
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
