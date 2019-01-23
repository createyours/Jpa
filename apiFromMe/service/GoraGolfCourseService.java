package org.leadingsoft.golf.api.service;

import java.util.ArrayList;
import java.util.List;

import org.leadingsoft.golf.api.entity.Rakutengolfcourseinfo;
import org.leadingsoft.golf.api.model.GolfCourseInfo;
import org.leadingsoft.golf.api.repository.RakutengolfcourseinfoRepository;
import org.leadingsoft.golf.api.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

@Service
public class GoraGolfCourseService {

  private final static Logger logger = LoggerFactory.getLogger(CollectService.class);

  @Value("${gora.search.url}")
  private String goraGolfSearchURL;

  private boolean firstInsert = true;

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private JdbcTemplate jdbcTemplate;
  
  @Autowired
  private RakutengolfcourseinfoRepository rakutengolfcourseinfoRepository;

  public void saveCourseInfo() {
//    if (firstInsert) {
//      firstInsert = false;
//      return;
//    }
    //确定是否是删除全部
	  
	  rakutengolfcourseinfoRepository.deleteAll();
	  
    JSONObject json = restTemplate.getForEntity(goraGolfSearchURL, JSONObject.class).getBody();
    String js = JSONObject.toJSONString(json.getJSONArray("Items"));
    List<GolfCourseInfo> courseInfoList = new ArrayList<>();
    courseInfoList = JSONObject.parseArray(js, GolfCourseInfo.class);
    int pageCount = json.getInteger("pageCount").intValue();
    for (int i = 2; i <= pageCount; i++) {
      String url = goraGolfSearchURL + "&page=" + i;
      JSONObject jsonRes = restTemplate.getForEntity(url, JSONObject.class).getBody();
      courseInfoList.addAll(JSONObject.parseArray(
          JSONObject.toJSONString(jsonRes.getJSONArray("Items")), GolfCourseInfo.class));
    }

    for (GolfCourseInfo courseInfo : courseInfoList) {
    	
    	Rakutengolfcourseinfo rakutengolfcourseinfo = new Rakutengolfcourseinfo();
    	rakutengolfcourseinfo.setGolfcourseid(nullToString(courseInfo.getGolfCourseId(), true));
    	rakutengolfcourseinfo.setGolfcoursename(nullToString(courseInfo.getGolfCourseName(), true));
    	rakutengolfcourseinfo.setGolfcourseabbr(nullToString(courseInfo.getGolfCourseAbbr(), true));
    	rakutengolfcourseinfoRepository.save(rakutengolfcourseinfo);
    }
    logger.info("insert RakutenGolfCourseInfo total ： " + courseInfoList.size());
  }

  
  
  /*public void saveCourseInfo() {
//    if (firstInsert) {
//      firstInsert = false;
//      return;
//    }
    String sql = null;
    sql = "DELETE FROM RakutenGolfCourseInfo";
    jdbcTemplate.execute(sql);
    JSONObject json = restTemplate.getForEntity(goraGolfSearchURL, JSONObject.class).getBody();
    String js = JSONObject.toJSONString(json.getJSONArray("Items"));
    List<GolfCourseInfo> courseInfoList = new ArrayList<>();
    courseInfoList = JSONObject.parseArray(js, GolfCourseInfo.class);
    int pageCount = json.getInteger("pageCount").intValue();
    for (int i = 2; i <= pageCount; i++) {
      String url = goraGolfSearchURL + "&page=" + i;
      JSONObject jsonRes = restTemplate.getForEntity(url, JSONObject.class).getBody();
      courseInfoList.addAll(JSONObject.parseArray(
          JSONObject.toJSONString(jsonRes.getJSONArray("Items")), GolfCourseInfo.class));
    }

    for (GolfCourseInfo courseInfo : courseInfoList) {
      sql = "INSERT INTO RakutenGolfCourseInfo(GolfCourseId, GolfCourseName, GolfCourseAbbr) VALUES (%s, %s, %s)";
      sql = String.format(sql, nullToString(courseInfo.getGolfCourseId(), true),
          nullToString(courseInfo.getGolfCourseName(), true),
          nullToString(courseInfo.getGolfCourseAbbr(), true));
      System.out.println(sql);
      jdbcTemplate.execute(sql);
    }
    logger.info("insert RakutenGolfCourseInfo total ： " + courseInfoList.size());
  }
*/
  private static String nullToString(String target, boolean isString) {
    if (StringUtils.isEmpty(target)) {
      return null;
    }
    return isString ? "'" + target + "'" : target;
  }
}
