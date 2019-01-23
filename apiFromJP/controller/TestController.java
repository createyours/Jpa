package org.leadingsoft.golf.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {
  private final static Logger logger = LoggerFactory.getLogger(TestController.class);
  @GetMapping("/hello")
  public String greeting() {
    logger.info("this is Security API");
      return "this is Security API";
  }
}
