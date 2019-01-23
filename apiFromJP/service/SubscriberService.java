package org.leadingsoft.golf.api.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.leadingsoft.golf.api.code.ApovStatusCode;
import org.leadingsoft.golf.api.code.CollectStateCode;
import org.leadingsoft.golf.api.code.MatchingCode;
import org.leadingsoft.golf.api.code.ResultCode;
import org.leadingsoft.golf.api.code.SexCode;
import org.leadingsoft.golf.api.entity.ApplyChat;
import org.leadingsoft.golf.api.entity.ApplyInfo;
import org.leadingsoft.golf.api.entity.MemberInfo;
import org.leadingsoft.golf.api.model.ApplyInfoDTO;
import org.leadingsoft.golf.api.model.DataResult;
import org.leadingsoft.golf.api.repository.ApplyChatRepository;
import org.leadingsoft.golf.api.repository.ApplyInfoRepository;
import org.leadingsoft.golf.api.repository.MemberInfoRepository;
import org.leadingsoft.golf.api.util.DataConvertUtils;
import org.leadingsoft.golf.api.util.DateLogicUtils;
import org.leadingsoft.golf.api.util.RandomUtils;
import org.leadingsoft.golf.api.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * 応募情報サービス
 *
 * <pre>
 *  登録・検索
 * </pre>
 */
@Service
public class SubscriberService {
  private final static Logger logger = LoggerFactory.getLogger(SubscriberService.class);
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private DateLogicUtils dateLogicUtils;

	// 応募情報リポジトリ
	@Autowired
	private ApplyInfoRepository applyInfoRepository;

	// 会員情報リポジトリ
	@Autowired
	private MemberInfoRepository memberInfoRepository;

	// チャットリポジトリ
	@Autowired
	private ApplyChatRepository applyChatRepository;

	/**
	 * 応募情報を保存する
	 *
	 * @param collect 募集情報
	 * @return 保存結果
	 */
	public DataResult<String> apply(ApplyInfoDTO applyInfo) {
		try {
			// 募集ID
			int roundSerialNo = 0;
			if (!StringUtils.isEmpty(applyInfo.getRoundSerialNo())) {
				roundSerialNo = Integer.parseInt(applyInfo.getRoundSerialNo());
			}
			// 指定されている会員ID、募集ID、承認ステータスの応募情報存在判断処理
			boolean existsFlg = applyInfoRepository.existsByMemberIdAndRoundSerialNoAndApovStatusIn(
					applyInfo.getMemberID(), roundSerialNo,
					Arrays.asList(new String[] { ApovStatusCode.UNApov.code(), ApovStatusCode.ApovOK.code() }));
			// 存在しない場合
			if (!existsFlg) {
				return new DataResult<String>(ResultCode.NG.code(), "該当する募集を応募しました。");
			}

			// 受付通番IDを生成する
			String regNo = "";
			while (true) {
				regNo = RandomUtils.getRandomNumber(8);
				boolean regExistFlg = applyInfoRepository.existsByRegNo(Integer.parseInt(regNo));
				// 該当受付番号IDが存在しない場合
				if (!regExistFlg) {
					break;
				}
			}

			// 応募情報の登録処理を行う
			ApplyInfo insertApplyInfo = new ApplyInfo();
			// 募集ID
			insertApplyInfo.setRoundSerialNo(roundSerialNo);
			// 会員ID
			insertApplyInfo.setMemberId(applyInfo.getMemberID());
			// 受付通番
			insertApplyInfo.setRegNo(Integer.valueOf(regNo));
			// 主催者へのコメント
			insertApplyInfo.setComments(applyInfo.getComments());
			// 承認ステータス
			insertApplyInfo.setApovStatus(ApovStatusCode.UNApov.code());
			// DB登録処理を行う
			applyInfoRepository.save(insertApplyInfo);

			// プロファイル情報を更新する
			String playYears = applyInfo.getPlayYears();
			if (StringUtils.isNotEmpty(playYears)) {
				playYears = playYears.replace("代", "");
				applyInfo.setPlayYears(playYears);
			}
			// 性別を設定する
			String strSex = applyInfo.getSex();
			if (StringUtils.isNotEmpty(strSex)) {
				strSex = strSex.equals(SexCode.BOY.codeNm()) ? SexCode.BOY.code() : SexCode.GIRL.code();
				applyInfo.setSex(strSex);
			}

			// 更新会員情報対象を取得する
			List<MemberInfo> updateMemberInfoList = memberInfoRepository.findByMemberID(applyInfo.getMemberID());
			MemberInfo updateMemberInfo = updateMemberInfoList.get(0);
			// 氏名
			updateMemberInfo.setName(applyInfo.getName());
			// カナ名
			updateMemberInfo.setKana(applyInfo.getKana());
			// 性別
			updateMemberInfo.setSex(applyInfo.getSex());
			// ゴルフ年数
			updateMemberInfo.setPlayYears(
					StringUtils.isEmpty(applyInfo.getPlayYears()) ? null : Integer.valueOf(applyInfo.getPlayYears()));
			// 携帯番号
			updateMemberInfo.setTelNo(applyInfo.getTelNo());
			// 都道府県
			updateMemberInfo.setName(applyInfo.getName());
			// 会員情報更新処理を行う
			memberInfoRepository.save(updateMemberInfo);
			return new DataResult<String>();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new DataResult<String>(ResultCode.NG.code(), "募集情報が登録失敗しました。");
		}
	}

	/**
	 * 応募情報を検索する
	 *
	 * @param roundSerialNo 募集ID
	 * @return 応募情報リスト
	 */
	public DataResult<List> search(String roundSerialNo) {
		List<ApplyInfo> applyInfoList = applyInfoRepository.findAll(new Specification<ApplyInfo>() {
			/** serialVersionUID */
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<ApplyInfo> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				CriteriaQuery<ApplyInfo> q1 = criteriaBuilder.createQuery(ApplyInfo.class);
				Join<MemberInfo, ApplyInfo> myOrderJoin = root.join("memberInfo", JoinType.INNER);
				q1.select(myOrderJoin).where(criteriaBuilder.equal(root.get("roundSerialNo"), roundSerialNo),
						criteriaBuilder.or(criteriaBuilder.isNull(root.get("apovStatus")),
								criteriaBuilder.notEqual(root.get("apovStatus"), ApovStatusCode.ApovNG.code())));
				return q1.getRestriction();
			}
		});

		// 応募情報存在しない場合、エラーメッセージを返却する
		if (applyInfoList == null || applyInfoList.size() == 0) {
			return new DataResult<List>(ResultCode.NG.code(), "応募情報がありません");
		}

		// 情報をHashMap形式に返却する
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		for (ApplyInfo applyInfo : applyInfoList) {
			try {
				// 会員情報以外の内容をMapに変換する
				Map<String, Object> map = DataConvertUtils.convertObjectToMap(applyInfo,
						new String[] { "memberInfo", "serialVersionUID" });
				// 会員情報を取得して、Mapに変換する
				MemberInfo memInfo = applyInfo.getMemberInfo();
				Map<String, Object> mapMember = DataConvertUtils.convertObjectToMap(memInfo,
						new String[] { "applyInfoList", "serialVersionUID", "applyChatList" });
				map.putAll(mapMember);
				mapList.add(map);
			} catch (Exception e) {
				logger.error(e.getMessage());
				return new DataResult<List>(ResultCode.NG.code(), "応募情報がありません");
			}
		}
		return new DataResult<List>(mapList);
	}

  /**
   * 応募情報を承認する
   *
   * @param roundSerialNo 募集ID
   * @param regNo 受付通番
   * @return
   */
  public DataResult<String> agree(String roundSerialNo, String regNo) {
    try {
      String sql = null;
      List<Map<String, Object>> list = null;
      // 該当する未承認の応募情報を検索する
      sql = "select 1 from ApplyInfo where RoundSerialNo= %s  AND RegNo = %s AND ApovStatus = '"
          + ApovStatusCode.UNApov.code() + "' ";
      sql = String.format(sql, nullToString(roundSerialNo, false), nullToString(regNo, false));
      logger.debug("select check ApplyInfo sql:==" + sql);
      list = jdbcTemplate.queryForList(sql);
      if (list == null || list.size() == 0) {
        return new DataResult<String>(ResultCode.NG.code(), "該当する応募情報が承認できません");
      }

      // 応募情報数をチェックする
      String collectcount = null;
      sql = "select (RecruitInfo.RecruitNum - IFNULL(Apply.ApovCnt, 0)) AS count "
          + " from RecruitInfo" + " left join  "
          + "      (select count(0) as ApovCnt, RoundSerialNo from ApplyInfo"
          + "       where ApovStatus = '1' group by RoundSerialNo) Apply "
          + "      ON Apply.RoundSerialNo = RecruitInfo.RoundSerialNo"
          + "      where RecruitInfo.RoundSerialNo = " + roundSerialNo;
      logger.debug("check ApplyInfo sql:==" + sql);
      list = jdbcTemplate.queryForList(sql);
      if (list == null || list.size() == 0) {
        return new DataResult<String>(ResultCode.NG.code(), "該当する応募情報が承認できません");
      } else {
        collectcount = StringUtils.toStr(list.get(0).get("count"));
        if (StringUtils.isEmpty(collectcount)
            || MatchingCode.UNMATCHED.code().equals(collectcount)) {
          // 募集が完了
          return new DataResult<String>(ResultCode.NG.code(), "この募集が募集完了しました。");
        }
      }

      // 応募情報を承認ずみへ変更する
      sql = "update ApplyInfo set ApovStatus = '" + ApovStatusCode.ApovOK.code()
          + "'  where RoundSerialNo= %s  AND RegNo = %s ";
      sql = String.format(sql, nullToString(roundSerialNo, false), nullToString(regNo, false));
      logger.debug("update ApplyInfo sql:==" + sql);
      jdbcTemplate.execute(sql);
      // 全部承認完了したら、この募集状態を更新する
      if (MatchingCode.MATCHED.code().equals(collectcount)) {
        sql = "update RecruitInfo set Status = " + CollectStateCode.COMPLETE.code()
            + " where RoundSerialNo= %s";
        sql = String.format(sql, nullToString(roundSerialNo, false));
        logger.debug("update RecruitInfo sql:==" + sql);
        jdbcTemplate.execute(sql);
      }
      return new DataResult<String>();
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new DataResult<String>(ResultCode.NG.code(), "応募情報が承認失敗しました。");
    }
  }

	/**
	 * チャット情報登録
	 *
	 * @param roundSerialNo 募集ID
	 * @param memberID 会員ID
	 * @param chatContents チャット内容
	 * @param regDate 登録日時
	 * 
	 * @return 登録メッセージ
	 */
	public DataResult<String> insertChat(String roundSerialNo, String memberID, String chatContents, String regDate) {
		int sortOrder = 0;
		// 指定募集IDの応募情報件数を取得する
		long sunNum = applyChatRepository.countByRoundSerialNo(Integer.parseInt(roundSerialNo));
		sortOrder = (int) sunNum + 1;

		// チャット情報登録処理
		ApplyChat applyChat = new ApplyChat();
		// 募集ID
		applyChat.setRoundSerialNo(Integer.valueOf(roundSerialNo));
		// 会員ID
		applyChat.setMemberId(memberID);
		// チャット内容
		applyChat.setChatContents(chatContents);
		// 表示順
		applyChat.setSortOrder(sortOrder);
		// 登録日時
		applyChat.setRegDate(regDate);
		applyChatRepository.save(applyChat);
		return new DataResult<String>(ResultCode.OK.code(), "チャット情報が登録しました。");
	}

	/**
	 * チャット情報取得
	 *
	 * @param roundSerialNo　募集ID
	 * @return　チャット情報
	 */
	public DataResult<List> searchChatInformation(String roundSerialNo) {

		// 募集IDより、チャット情報リストを取得する
		List<ApplyChat> applyChatList = applyChatRepository.findAll(new Specification<ApplyChat>() {
			/** serialVersionUID */
			private static final long serialVersionUID = 3293797945759768197L;

			@Override
			public Predicate toPredicate(Root<ApplyChat> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				CriteriaQuery<ApplyChat> q1 = criteriaBuilder.createQuery(ApplyChat.class);
				Join<MemberInfo, ApplyChat> myOrderJoin = root.join("memberInfo", JoinType.INNER);
				q1.select(myOrderJoin).where(criteriaBuilder.equal(root.get("roundSerialNo"), roundSerialNo));
				return q1.getRestriction();
			}
		}, new Sort(Direction.DESC, "sortOrder"));

		// 検索結果がない場合、空白リストを返却する
		if (null == applyChatList || applyChatList.size() == 0) {
			return new DataResult<List>(applyChatList);
		}

		// データが存在する場合、マップに変換して返却する
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (ApplyChat applyChat : applyChatList) {
			try {
				Map<String, Object> applyMap = DataConvertUtils.convertObjectToMap(applyChat,
						new String[] { "serialVersionUID", "memberInfo" });
				// 会員情報をマップに変換する
				Map<String, Object> memMap = DataConvertUtils.convertObjectToMap(applyChat.getMemberInfo(),
						new String[] { "serialVersionUID", "applyInfoList", "applyChatList" });
				applyMap.putAll(memMap);
				list.add(applyMap);
			} catch (Exception e) {
				logger.error(e.getMessage());
				return new DataResult<List>(ResultCode.NG.code(), "チャット情報が取得しません。");
			}
		}
		return new DataResult<List>(list);
	}

	/**
	 * チャット情報取得
	 *
	 * @param memberId　会員ID
	 * @return　チャット情報
	 */
	public DataResult<List> getNewChatMassage(String memberId) {
		// 指定された会員IDより、チャット情報を取得する
		List<Map<String, Object>> list = applyChatRepository.getApplyInfoByMemberId(memberId);
		return new DataResult<List>(list);
	}

  private static String nullToString(String target, boolean isString) {
    if (StringUtils.isEmpty(target)) {
      return null;
    }
    return isString ? "'" + target + "'" : target;
  }

}
