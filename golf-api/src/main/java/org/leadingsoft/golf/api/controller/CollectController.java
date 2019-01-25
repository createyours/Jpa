package org.leadingsoft.golf.api.controller;

import java.util.List;

import org.leadingsoft.golf.api.model.Collect;
import org.leadingsoft.golf.api.model.CollectSearchResult;
import org.leadingsoft.golf.api.model.DataResult;
import org.leadingsoft.golf.api.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 募集情報処理コントロール
 *
 * <pre>
 *  募集情報の登録
 *  募集情報の検索
 * </pre>
 */
@RestController
@RequestMapping("/api/public/collect")
public class CollectController {
  /** 募集情報サービス */
  @Autowired
  private CollectService collectService;

  /**
   * 募集情報を保存する
   *
   * @param collect 募集情報
   * @return 登録結果
   */
  @RequestMapping("/save")
  public DataResult<String> doSaveCollectInfo(@RequestBody Collect collect) {
    return collectService.save(collect);
  }

  /**
   * 募集情報を検索する
   *
   * @param playDate プレー日
   * @param area エリア
   * @param golfBarName ゴルフ場名
   * @param pageMode 0:前頁 1:次頁
   * @param memberId ログイン中のユーザID
   * @param roundSerialNo 募集ID
   * @return 検索結果
   */
  @RequestMapping("/search")
  @ResponseBody
  public DataResult<CollectSearchResult> doSearchCollectInfo(
      @RequestParam("playDate") String playDate, @RequestParam("area") String area,
      @RequestParam("golfBarName") String golfBarName, @RequestParam("pageMode") String pageMode,
      @RequestParam("memberId") String memberId,
      @RequestParam("roundSerialNo") String roundSerialNo) {
    return collectService.search(playDate, area, golfBarName, pageMode, memberId, roundSerialNo);
  }

  /**
   * 自分の応募情報を取消する
   *
   * @param playDate
   * @param area
   * @param golfBarName
   * @param pageMode
   * @param memberId
   * @param roundSerialNo
   * @return
   */
  @RequestMapping("/cancel")
  @ResponseBody
  public DataResult<CollectSearchResult> doCancel(@RequestParam("playDate") String playDate,
      @RequestParam("area") String area, @RequestParam("golfBarName") String golfBarName,
      @RequestParam("pageMode") String pageMode, @RequestParam("memberId") String memberId,
      @RequestParam("roundSerialNo") String roundSerialNo) {
    return collectService.cancel(playDate, area, golfBarName, pageMode, memberId, roundSerialNo);
  }

  /**
   * 自分の応募情報を削除する
   *
   * @param roundSerialNo
   * @return
   */
  @RequestMapping("/deleteSelfApplyInfo")
  @ResponseBody
  public DataResult<CollectSearchResult> deletSelfApplyInfo(
      @RequestParam("memberId") String memberId,
      @RequestParam("roundSerialNo") String roundSerialNo) {
    return collectService.deleteSelfApplyInfo(roundSerialNo, memberId);
  }

  /**
   * 自分の情報を検索する
   *
   * @param memberId
   * @param collectFlg
   * @return
   */
  @RequestMapping("/searchSelf")
  @ResponseBody
  public DataResult<List> doSearchSelfData(@RequestParam("memberId") String memberId,
      @RequestParam("collectFlg") String collectFlg) {
    return collectService.searchSelfData(memberId, collectFlg);
  }

  /**
   * 募集情報を更新する
   *
   * @param collect
   * @return
   */
  @RequestMapping("/updateDetail")
  @ResponseBody
  public DataResult<String> updateDetail(@RequestBody Collect collect) {
    return collectService.updateDetail(collect);
  }

  /**
   * 募集情報を更新する(delFlg)
   *
   * @param collect
   * @return
   */
  @RequestMapping("/updateDelFlg")
  @ResponseBody
  public DataResult<String> updateDelFlg(@RequestParam("roundSerialNo") String roundSerialNo) {
    return collectService.updateDelFlg(roundSerialNo);
  }

  /**
   * ゴルフ情報を取る
   *
   * @return
   */
  @RequestMapping("/searchRakutenGolfCourseInfo")
  @ResponseBody
  public DataResult<List> searchRakutenGolfCourseInformation() {
    return collectService.searchRakutenGolfCourseInformation();
  }

}
