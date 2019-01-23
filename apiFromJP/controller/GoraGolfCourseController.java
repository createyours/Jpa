package org.leadingsoft.golf.api.controller;

import org.leadingsoft.golf.api.service.GoraGolfCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableScheduling
public class GoraGolfCourseController {

  @Autowired
  private GoraGolfCourseService goraGolfCourseService;

  @Scheduled(cron = "0 59 2 3 L * ?")
  public void saveGolfDetail() {
    goraGolfCourseService.saveCourseInfo();
  }

}
