package org.leadingsoft.golf.api;

import org.leadingsoft.golf.api.repository.BaseRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Springアプリ起動用クラス.
 */




@EnableJpaRepositories(basePackages = {"org.leadingsoft.golf.api.repository"},
repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)//指定自己的工厂类
@SpringBootApplication
public class GolfApiApplication {

  /**
   * メインメソッド
   * 
   * @param args パラメータ
   */
  public static void main(String[] args) {
    SpringApplication.run(GolfApiApplication.class, args);
  }
}
