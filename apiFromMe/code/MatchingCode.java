package org.leadingsoft.golf.api.code;

public enum MatchingCode {
  // C0004
  UNMATCHED("0"), MATCHED("1");

  private String code_;

  MatchingCode(String code) {
    code_ = code;
  }

  public String code() {
    return code_;
  }
}
