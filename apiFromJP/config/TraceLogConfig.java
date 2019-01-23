//****************************************************************************//
// システム         : Golf
//----------------------------------------------------------------------------//
//                (c)Copyright 2018 LeadingSoft All rights reserved.
//============================================================================//

package org.leadingsoft.golf.api.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <pre>
 * Service呼び出し
 * 出力Profileはdefault, it
 * </pre>
 */
@Configuration
@EnableAspectJAutoProxy
public class TraceLogConfig {

  /**
   * <pre>
   * コンポーネント作成。
   * </pre>
   * 
   * @return コンポーネント
   */
  @Bean
  TraceInterceptor traceInterceptor() {
    return new TraceInterceptor();
  }

  /**
   * <pre>
   * Service層のメソッドを呼び出しの際のパラメータと実行時間ログを出力する。
   * </pre>
   */
  @Aspect
  public class TraceInterceptor {
    /** JSON ObjectMapper */
    @Autowired
    private ObjectMapper objectMapper;

    /** ログ出力クラス。 */
    protected Logger logger = LoggerFactory.getLogger("ACS");

    /** ログ出力形式。 */
    private static final String LOG_FMT = "call:{}, args:{}, elapsed:{}";

    /**
     * <pre>
     * Service呼び出しの際の実行時間ログを出力する。
     * </pre>
     * 
     * @param pjp ProceedingJoinPointクラス
     * @return 元返却結果
     * @throws Throwable 例外
     */
    @Around("execution(public * org.leadingsoft..service..*Service.*(..))")
    public Object invoke(ProceedingJoinPoint pjp) throws Throwable {
      final String className = pjp.getTarget().getClass().getSimpleName();
      final Signature signature = pjp.getStaticPart().getSignature();

      String methodName = "";
      Class<?>[] parameterTypes = null;
      if (signature instanceof MethodSignature) {
        MethodSignature methodSignature = (MethodSignature) signature;
        parameterTypes = methodSignature.getParameterTypes();
        methodName = methodSignature.getName();
      }
      Object[] args = pjp.getArgs();

      Map<String, Object> argsMap = new LinkedHashMap<>();
      for (int i = 0; i < args.length; i++) {
        argsMap.put(i + 1 + "_" + parameterTypes[i].getSimpleName(), args[i]);
      }

      Long startNanoTime = System.nanoTime();
      try {
        return pjp.proceed();
      } finally {
        logger.info(LOG_FMT, className + "#" + methodName, objectMapper.writeValueAsString(argsMap),
            formattingElapsedTime(System.nanoTime() - startNanoTime));
      }
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
}
