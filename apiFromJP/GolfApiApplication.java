package org.leadingsoft.golf.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Springアプリ起動用クラス.
 */
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
