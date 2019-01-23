//****************************************************************************//
// システム         : Golf
//----------------------------------------------------------------------------//
//                (c)Copyright 2018 LeadingSoft All rights reserved.
//============================================================================//

package org.leadingsoft.golf.api.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * <pre>
 * Controllerクラスの共通インタセプター
 * 
 * ・リクエストのアクセスログを出力
 * ・メソッド種類判別（例外処理用）
 * </pre>
 *
 */
@Component
public class ControllerInterceptor extends HandlerInterceptorAdapter {

  /** アクセスログ出力クラス。 */
  private static final Logger LOGGER = LoggerFactory.getLogger("ACS");

  /** アクセスログ形式。 */
  private static final String LOG_FMT = "url:{}, method:{}, status:{}, elapsed:{}";

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter# preHandle(
   * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   * java.lang.Object)
   */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    // Controller以外のアクセス（css,img,js等）は処理をスキップ
    if (!(handler instanceof HandlerMethod)) {
      return true;
    }

    HandlerMethod handlerMethod = (HandlerMethod) handler;

    // クラスが「RestController」やメソッドに「ResponseBody」アノテーションが定義されている場合、JSONレスポンスとします
    if (handlerMethod.getBeanType().isAnnotationPresent(RestController.class) //
        || handlerMethod.hasMethodAnnotation(ResponseBody.class)) {
      request.setAttribute(RequestContext.RESPONSE_IS_JSON, true);
    }

    // 処理時間算出用の開始時間セット
    request.setAttribute(RequestContext.START_CONTROLLER_NANO_TIME, System.nanoTime());

    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#
   * afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   * java.lang.Object, java.lang.Exception)
   */
  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {
    Long startNanoTime = (Long) request.getAttribute(RequestContext.START_CONTROLLER_NANO_TIME);
    String elapsedTme = "";
    if (startNanoTime != null) {
      long endNanoTime = System.nanoTime();
      elapsedTme = formattingElapsedTime(endNanoTime - startNanoTime);
    }

    if (!request.getRequestURI().endsWith("/health")) { // ヘルスチェックURLをスキップ
      LOGGER.info(LOG_FMT, request.getRequestURI(), request.getMethod(), response.getStatus(),
          elapsedTme);
    }

    request.removeAttribute(RequestContext.RESPONSE_IS_JSON);
  }

  /**
   * ナノ秒をX.XXXXXXと秒を基準とした文字列に変換します。 除算を行わずに実施し、高速化を行っています。
   *
   * @param elapsedTime ナノ秒
   * @return 秒単位に整形された文字列
   */
  private String formattingElapsedTime(long elapsedTime) {
    StringBuilder builder = new StringBuilder();
    builder.append(elapsedTime);
    if (builder.length() < 10) {
      for (int j = builder.length(); j < 10; j++) {
        if (j == 9) {
          builder.insert(0, '.');
        }
        builder.insert(0, '0');
      }
    } else {
      builder.insert(builder.length() - 9, '.');
    }
    return builder.toString();
  }
}
