//****************************************************************************//
// システム         : Golf
//----------------------------------------------------------------------------//
//                (c)Copyright 2018 LeadingSoft All rights reserved.
//============================================================================//

package org.leadingsoft.golf.api.config;

/**
 * <pre>
 * {@link HttpServletRequest}におけるコンテキト情報。
 * </pre>
 *
 */
public final class RequestContext {

  /** 時間測るためのrequest context key */
  public static final String START_CONTROLLER_NANO_TIME = "start_elapsed_nano_time";

  /** レスポンスがJSONかどうかフラグ */
  public static final String RESPONSE_IS_JSON = "response_is_json";

}
