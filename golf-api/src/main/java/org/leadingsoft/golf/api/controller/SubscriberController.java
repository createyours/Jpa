package org.leadingsoft.golf.api.controller;

import java.util.List;

import org.leadingsoft.golf.api.model.ApplyInfo;
import org.leadingsoft.golf.api.model.DataResult;
import org.leadingsoft.golf.api.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 応募者情報処理コントロール
 *
 * <pre>
 *  応募者情報の登録
 *  応募者情報の検索
 * </pre>
 */
@RestController
@RequestMapping("/api/public/Subscriber")
public class SubscriberController {
  /** ログインサビス */
  @Autowired
  private SubscriberService subscriberService;

  /**
   * 応募者情報登録でプロフィール情報を更新する・応募情報を登録する
   *
   * @param applyInfo 応募情報
   * @return 登録結果
   */
  @RequestMapping("/apply")
  public DataResult<String> doAddSubscriber(@RequestBody ApplyInfo applyInfo) {
    return subscriberService.apply(applyInfo);
  }

  /**
   * 応募情報を検索する
   *
   * @param roundSerialNo 募集ID
   * @param regNo 受付通番
   * @return
   */
  @RequestMapping("/search")
  @ResponseBody
  public DataResult<List> doSearch(@RequestParam("roundSerialNo") String roundSerialNo) {
    return subscriberService.search(roundSerialNo);
  }

  /**
   * 応募情報を承認する
   *
   * @param roundSerialNo 募集ID
   * @param regNo 受付通番
   * @return
   */
  @RequestMapping("/agree")
  @ResponseBody
  public DataResult<String> doAgree(@RequestParam("roundSerialNo") String roundSerialNo,
      @RequestParam("regNo") String regNo) {
    return subscriberService.agree(roundSerialNo, regNo);
  }

  /**
   * チャット情報登録
   *
   * @param roundSerialNo
   * @param memberID
   * @param chatContents
   * @return
   */
  @RequestMapping("/insertChat")
  @ResponseBody
  public DataResult<String> insertChat(@RequestParam("roundSerialNo") String roundSerialNo,
      @RequestParam("memberID") String memberID,
      @RequestParam("chatContents") String chatContents) {
    return subscriberService.insertChat(roundSerialNo, memberID, chatContents);
  }

  /**
   * チャット情報取得
   *
   * @param roundSerialNo
   * @return
   */
  @RequestMapping("/searchChatInformation")
  @ResponseBody
  public DataResult<List> searchChatInformation(
      @RequestParam("roundSerialNo") String roundSerialNo) {
    return subscriberService.searchChatInformation(roundSerialNo);
  }

}
