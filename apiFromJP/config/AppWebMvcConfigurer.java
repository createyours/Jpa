//****************************************************************************//
// システム         : Golf
//----------------------------------------------------------------------------//
//                (c)Copyright 2018 LeadingSoft All rights reserved.
//============================================================================//

package org.leadingsoft.golf.api.config;

import java.nio.file.Paths;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <pre>
 * 
 * MVCに関す共通設定
 * ・Interceptor有効化設定
 * 
 * </pre>
 */
@Configuration
public class AppWebMvcConfigurer implements WebMvcConfigurer {

  /** アクセスログ出力 */
  @Autowired
  ControllerInterceptor accessLogInterceptor;

  @Value("${upload.icon.dir}")
  private String iconDir;

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurer# addInterceptors(org.
   * springframework.web.servlet.config.annotation.InterceptorRegistry)
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(accessLogInterceptor) //
        .addPathPatterns("/**") //
        .excludePathPatterns("/vendors/**", //
            "/script/**", //
            "/stylesheet/**", //
            "/image/**", //
            "/build/**");
  }

  /**
   * RestTemplate DI 設定
   * 
   * @return RestTemplate
   */
  @Bean
  public RestTemplate getRestClient() {
    RestTemplate restClient = new RestTemplate(
        new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));

    return restClient;
  }

  // 静的リソース の設定
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    String iconPatch = Paths.get(iconDir).toAbsolutePath().normalize().toUri().toString();
    LoggerFactory.getLogger(AppWebMvcConfigurer.class).info("iconPatch=" + iconPatch);
    registry.addResourceHandler("/images/**").addResourceLocations(iconPatch);
  }
}
