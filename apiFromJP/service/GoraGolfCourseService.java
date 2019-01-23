package org.leadingsoft.golf.api.service;

import java.util.ArrayList;
import java.util.List;

import org.leadingsoft.golf.api.entity.RakutenGolfCourseInfo;
import org.leadingsoft.golf.api.model.GolfCourseInfo;
import org.leadingsoft.golf.api.repository.RakutenGolfCourseInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

  // 楽天ゴルフ場基本情報リポジトリ 
  @Autowired
  private RakutenGolfCourseInfoRepository repository;

  public void saveCourseInfo() {
    if (firstInsert) {
      firstInsert = false;
      return;
    }
    // 楽天ゴルフ場基本情報全テーブル削除処理
    repository.deleteAll();
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

    // 楽天ゴルフ場基本情報登録処理
    // APIから取得された情報を楽天ゴルフ場基本情報エンティティに設定する
    List<RakutenGolfCourseInfo> rakutenGolfLst = new ArrayList<RakutenGolfCourseInfo>();
    for (GolfCourseInfo courseInfo : courseInfoList) {
    	RakutenGolfCourseInfo rakutenGolf = new RakutenGolfCourseInfo();
    	// ゴルフ場ID
    	rakutenGolf.setGolfCourseId(courseInfo.getGolfCourseId());
    	// ゴルフ場名
    	rakutenGolf.setGolfCourseName(courseInfo.getGolfCourseName());
    	// ゴルフ場名(略称)
    	rakutenGolf.setGolfCourseAbbr(courseInfo.getGolfCourseAbbr());
    	rakutenGolfLst.add(rakutenGolf);
    }
    // DBに登録する
    repository.saveAll(rakutenGolfLst);
    logger.info("insert RakutenGolfCourseInfo total ： " + courseInfoList.size());
  }
}
