//****************************************************************************//
// システム         : Golf
//----------------------------------------------------------------------------//
//                (c)Copyright 2018 LeadingSoft All rights reserved.
//============================================================================//

package org.leadingsoft.golf.api.config;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * メッセージ共通
 * </pre>
 */
@Component
public class Messages {

  /** メッセージリソース */
  @Autowired
  private MessageSource messageSource;

  /**
   * コードにより、メッセージ内容を取得
   * 
   * @param code コード
   * @return
   */
  public String getMessage(String code) {
    return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
  }

  /**
   * コードにより、メッセージ内容を取得。パラメータあり
   * 
   * @param code コード
   * @param args パラメータ
   * @return
   */
  public String getMessage(String code, String... args) {
    return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
  }

  /**
   * JSON形式エラーオブジェクトを返す
   * 
   * @param code エラーコード
   * @param args パラメーター。省略可
   * @return
   */
  public HashMap<String, String> getJsonMessage(String code, String... args) {
    HashMap<String, String> errorHash = new HashMap<String, String>();
    errorHash.put("code", code);
    errorHash.put("message", getMessage(code, args));
    return errorHash;
  }

}
