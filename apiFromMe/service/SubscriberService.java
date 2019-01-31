package org.leadingsoft.golf.api.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.leadingsoft.golf.api.code.ApovStatusCode;
import org.leadingsoft.golf.api.code.CollectStateCode;
import org.leadingsoft.golf.api.code.MatchingCode;
import org.leadingsoft.golf.api.code.ResultCode;
import org.leadingsoft.golf.api.code.SexCode;
import org.leadingsoft.golf.api.entity.ApplyChat;
import org.leadingsoft.golf.api.entity.Applyinfo;
import org.leadingsoft.golf.api.entity.MemberInfo;
import org.leadingsoft.golf.api.model.ApplyInfo;
import org.leadingsoft.golf.api.model.DataResult;
import org.leadingsoft.golf.api.repository.ApplyInfoRepository;
import org.leadingsoft.golf.api.repository.ApplyChatRepository;
import org.leadingsoft.golf.api.repository.MemberInfoRepository;
import org.leadingsoft.golf.api.repository.RecruitInfoRepository;
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
  
  @Autowired
  private ApplyInfoRepository applyInfoRepository;
  
  @Autowired
  private MemberInfoRepository memberInfoRepository;
  
  @Autowired
  private ApplyChatRepository applychatRepository;

  @Autowired RecruitInfoRepository recruitInfoRepository;
  /**
   * 応募情報を保存する
   *
   * @param collect 募集情報
   * @return 保存結果
   */
  public DataResult<String> apply(ApplyInfo applyInfo) {
	    try {
	    	
	    	Specification<Applyinfo> specification = new Specification<Applyinfo>(){

				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<Applyinfo> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					
					List<Predicate> predicateList = new ArrayList<Predicate>();	
					
					Path<String> exp1 = root.get("memberId");
					Path<Integer> exp2 = root.get("roundSerialNo");
					Path<String> exp3 = root.get("apovStatus");
					
					CriteriaBuilder.In<String> in =  criteriaBuilder.in(exp3);
					in.value(nullToString(ApovStatusCode.UNApov.code(), true));
					in.value(nullToString(ApovStatusCode.ApovOK.code(), true));
					predicateList.add(criteriaBuilder.equal(exp1.as(String.class),nullToString(applyInfo.getMemberID(), true)));
					predicateList.add(criteriaBuilder.equal(exp2.as(Integer.class),nullToString(applyInfo.getRoundSerialNo(), false)));
					predicateList.add(in);
					
					Predicate[] p = new Predicate[predicateList.size()];
					return criteriaBuilder.and(predicateList.toArray(p));
				}
	    	};
	    	List<Applyinfo> list = applyInfoRepository.findAll(specification);
	    	
	    	
	      if (list != null && list.size() > 0) {
	        return new DataResult<String>(ResultCode.NG.code(), "該当する募集を応募しました。");
	      }

	      // 受付通番IDを生成する
	      String regNo;
	      while (true) {
	        regNo = RandomUtils.getRandomNumber(8);
	        
	        List<Applyinfo> applyinfoList = applyInfoRepository.findByRegNo(Integer.parseInt(regNo));
	        
	        if (applyinfoList == null || applyinfoList.size() == 0) {
	          break;
	        }
	      }
	      
	      Applyinfo applyinfo = new Applyinfo();
	      applyinfo.setRoundSerialNo(Integer.parseInt(nullToString(applyInfo.getRoundSerialNo(), false)));
	      applyinfo.setMemberId(nullToString(applyInfo.getMemberID(), true));
	      applyinfo.setRegNo(Integer.parseInt(nullToString(regNo, false)));
	      applyinfo.setComments(nullToString(applyInfo.getComments(), true));
	      applyinfo.setApovStatus(nullToString(ApovStatusCode.UNApov.code(), true));
	      //存入的数据中regno，和roundserialno数值有变
	      applyInfoRepository.save(applyinfo);

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
	      
	      List<MemberInfo> memberInfoList = memberInfoRepository.findByMemberID(nullToString(applyInfo.getMemberID(), true));
	      MemberInfo memberInfo = memberInfoList.get(0);
	      memberInfo.setName(nullToString(applyInfo.getName(), true));
	      memberInfo.setKana(nullToString(applyInfo.getKana(), true));
	      memberInfo.setSex(nullToString(applyInfo.getSex(), true));
	      memberInfo.setPlayYears(Integer.valueOf(nullToString(applyInfo.getPlayYears(),false)));
	      memberInfo.setTelNo(nullToString(applyInfo.getTelNo(), true));
	      memberInfo.setState(nullToString(applyInfo.getState(), true));
	      memberInfoRepository.save(memberInfo);
	      
	      return new DataResult<String>();
	    } catch (Exception e) {
	      logger.error(e.getMessage());
	      return new DataResult<String>(ResultCode.NG.code(), "募集情報が登録失敗しました。");
	    }
	  }
  
 /* public DataResult<String> apply(ApplyInfo applyInfo) {
    try {
      String sql = null;
      List<Map<String, Object>> list = null;
      sql = "select 1 from ApplyInfo where MemberID = %s  AND RoundSerialNo= %s  AND ApovStatus in(%s,%s) ";
      sql = String.format(sql, nullToString(applyInfo.getMemberID(), true),
          nullToString(applyInfo.getRoundSerialNo(), false),
          nullToString(ApovStatusCode.UNApov.code(), true),
          nullToString(ApovStatusCode.ApovOK.code(), true));
      logger.debug("select ApplyInfo sql:==" + sql);
      // この応募は該当するユーザを承認済ですか？
      list = jdbcTemplate.queryForList(sql);
      if (list != null && list.size() > 0) {
        return new DataResult<String>(ResultCode.NG.code(), "該当する募集を応募しました。");
      }

      // 受付通番IDを生成する
      String regNo;
      while (true) {
        regNo = RandomUtils.getRandomNumber(8);
        sql = "select 1 from ApplyInfo where RegNo = " + regNo;
        logger.debug("select ApplyInfo2 sql:==" + sql);
        list = jdbcTemplate.queryForList(sql);
        if (list == null || list.size() == 0) {
          break;
        }
      }
      sql = "INSERT INTO ApplyInfo (RoundSerialNo, MemberID, RegNo, Comments, ApovStatus) VALUES (%s, %s, %s, %s, %s)";
      sql = String.format(sql, nullToString(applyInfo.getRoundSerialNo(), false),
          nullToString(applyInfo.getMemberID(), true), nullToString(regNo, false),
          nullToString(applyInfo.getComments(), true),
          nullToString(ApovStatusCode.UNApov.code(), true));
      logger.debug("insert ApplyInfo sql:==" + sql);
      jdbcTemplate.execute(sql);

      // プロファイル情報を更新する
      sql = "UPDATE MemberInfo SET  Name=%s, Kana=%s, Sex=%s,"
          + "  PlayYears=%s,TelNo=%s, State=%s " + "WHERE MemberID=%s";

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
      sql = String.format(sql, nullToString(applyInfo.getName(), true),
          nullToString(applyInfo.getKana(), true), nullToString(applyInfo.getSex(), true),
          nullToString(applyInfo.getPlayYears(), false), nullToString(applyInfo.getTelNo(), true),
          nullToString(applyInfo.getState(), true), nullToString(applyInfo.getMemberID(), true));
      logger.debug("update MemberInfo sql:==" + sql);
      jdbcTemplate.execute(sql);
      return new DataResult<String>();
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new DataResult<String>(ResultCode.NG.code(), "募集情報が登録失敗しました。");
    }
  }*/

  /**
   * 応募情報を検索する
   *
   * @param roundSerialNo 募集ID
   * @return
   */
  public DataResult<List> search(String roundSerialNo) {
		      // 該当する未承認の応募情報を検索する
	    	Specification<Applyinfo> specification = new Specification<Applyinfo>(){

				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<Applyinfo> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					
					List<Predicate> predicateList = new ArrayList<Predicate>();	
					
					CriteriaQuery<Applyinfo> applyinfoQuery = criteriaBuilder.createQuery(Applyinfo.class);
					Join<MemberInfo,Applyinfo> join = root.join("memberInfo",JoinType.INNER);
					
					Path<String> exp1 = root.get("roundSerialNo");
					Path<Integer> exp2 = root.get("apovStatus");
					
					predicateList.add(criteriaBuilder.equal(exp1.as(Integer.class), Integer.parseInt(nullToString(roundSerialNo, false))));
					predicateList.add(criteriaBuilder.or(criteriaBuilder.isNull(exp2),criteriaBuilder.notEqual(exp2, ApovStatusCode.ApovNG.code())));
					
					Predicate[] p = new Predicate[predicateList.size()];
					return applyinfoQuery.select(join).where(criteriaBuilder.and(predicateList.toArray(p))).getRestriction();
				}
	    	};
	    	List<Applyinfo> applyinfoList = applyInfoRepository.findAll(specification);
	    	
	      if (applyinfoList == null || applyinfoList.size() == 0) {
	        return new DataResult<List>(ResultCode.NG.code(), "応募情報がありません");
	      }

			// 情報をHashMap形式に返却する
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (Applyinfo applyInfo : applyinfoList) {
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
  
  
/*  public DataResult<List> search(String roundSerialNo) {
    try {

    	
      String sql = null;
      List<Map<String, Object>> list = null;
      // 該当する未承認の応募情報を検索する
      sql = "select MemberInfo.*, ApplyInfo.RoundSerialNo,ApplyInfo.RegNo,ApplyInfo.ApovStatus,ApplyInfo.Comments from ApplyInfo inner join MemberInfo on MemberInfo.MemberID = ApplyInfo.MemberID  where RoundSerialNo= %s  AND (ApovStatus is null or ApovStatus != '"
          + ApovStatusCode.ApovNG.code() + "') ";
      sql = String.format(sql, nullToString(roundSerialNo, false));
      logger.debug("search ApplyInfo sql:==" + sql);
      list = jdbcTemplate.queryForList(sql);
      if (list == null || list.size() == 0) {
        return new DataResult<List>(ResultCode.NG.code(), "応募情報がありません");
      }
      return new DataResult<List>(list);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new DataResult<List>(ResultCode.NG.code(), "応募情報が取得失敗しました。");
    }
  }*/

  /**
   * 応募情報を承認する
   *
   * @param roundSerialNo 募集ID
   * @param regNo 受付通番
   * @return
   */
  
  public DataResult<String> agree(String roundSerialNo, String regNo) {
	    try {
	      // 該当する未承認の応募情報を検索する
	    	boolean existsFlag = applyInfoRepository.existsByRoundSerialNoAndRegNoAndApovStatus(Integer.valueOf(roundSerialNo), Integer.valueOf(regNo), ApovStatusCode.UNApov.code());
	    	
	      if (!existsFlag) {
	        return new DataResult<String>(ResultCode.NG.code(), "該当する応募情報が承認できません");
	      }
	      
	      String collectcount = null;
	      List<Integer> list =new ArrayList<Integer>();
	      list = recruitInfoRepository.getCountByRoundSerialNo(Integer.valueOf(roundSerialNo));

	      
	      if (list == null || list.size() == 0) {
	        return new DataResult<String>(ResultCode.NG.code(), "該当する応募情報が承認できません");
	      } else {
	        collectcount = StringUtils.toStr(list.get(0));
	        if (StringUtils.isEmpty(collectcount)
	            || MatchingCode.UNMATCHED.code().equals(collectcount)) {
	          // 募集が完了
	          return new DataResult<String>(ResultCode.NG.code(), "この募集が募集完了しました。");
	        }
	      }
	      
	      // 応募情報を承認ずみへ変更する
	      int updateNumber = applyInfoRepository.updateApplyInfo(ApovStatusCode.ApovOK.code(),Integer.valueOf(roundSerialNo), Integer.valueOf(regNo));
	      // 全部承認完了したら、この募集状態を更新する
	      if (MatchingCode.MATCHED.code().equals(collectcount)) {

	    	  recruitInfoRepository.updateRecruitInfo(Integer.valueOf(CollectStateCode.COMPLETE.code()), Integer.valueOf(roundSerialNo));
	      }
	      return new DataResult<String>();
	    } catch (Exception e) {
	      logger.error(e.getMessage());
	      return new DataResult<String>(ResultCode.NG.code(), "応募情報が承認失敗しました。");
	    }
	  }
  
 /* public DataResult<String> agree(String roundSerialNo, String regNo) {
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
  }*/

  /**
   * チャット情報登録
   *
   * @param roundSerialNo
   * @param memberID
   * @param chatContents
   * @return
   */
  public DataResult<String> insertChat(String roundSerialNo, String memberID, String chatContents) {
	    int sortOrder = 0;
	    int sunNum = applychatRepository.countByRoundSerialNo(Integer.parseInt(roundSerialNo));//是否有类型转换
	    sortOrder = sunNum + 1;

	    Calendar cal = dateLogicUtils.getCurrent();
	    String insTstmp = dateLogicUtils.getCurrentTimeString(cal);
	    
	    ApplyChat applychat = new ApplyChat();
	    applychat.setRoundSerialNo(Integer.parseInt(nullToString(roundSerialNo, false)));
	    applychat.setMemberId(nullToString(memberID, true));
	    applychat.setChatContents(nullToString(chatContents, true));
	    applychat.setSortOrder(Integer.parseInt(nullToString(String.valueOf(sortOrder), false)));
	    applychat.setRegDate(nullToString(insTstmp, true));
	    applychatRepository.save(applychat);
	    
	    return new DataResult<String>(ResultCode.OK.code(), "チャット情報が登録しました。");
	  }
  
  /*public DataResult<String> insertChat(String roundSerialNo, String memberID, String chatContents) {
    int sortOrder = 0;
    String sql = "SELECT count(*) as sumNum from ApplyChat where RoundSerialNo = %s";
    sql = String.format(sql, nullToString(roundSerialNo, false));
    List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
    int sunNum = Integer.parseInt(list.get(0).get("sumNum").toString());
    sortOrder = sunNum + 1;

    Calendar cal = dateLogicUtils.getCurrent();
    String insTstmp = dateLogicUtils.getCurrentTimeString(cal);
    sql = "INSERT ApplyChat(RoundSerialNo, MemberID, ChatContents, SortOrder, RegDate) VALUES (%s, %s, %s, %s, %s)";
    sql = String.format(sql, nullToString(roundSerialNo, false), nullToString(memberID, true),
        nullToString(chatContents, true), nullToString(String.valueOf(sortOrder), false),
        nullToString(insTstmp, true));
    jdbcTemplate.execute(sql);
    return new DataResult<String>(ResultCode.OK.code(), "チャット情報が登録しました。");
  }*/

  /**
   * チャット情報取得
   *
   * @param roundSerialNo
   * @return
   */
  
  public DataResult<List> searchChatInformation(String roundSerialNo) {
	    	
	    	Specification<ApplyChat> specification = new Specification<ApplyChat>(){
				private static final long serialVersionUID = 1L;
				
				@Override
				public Predicate toPredicate(Root<ApplyChat> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {

					CriteriaQuery<ApplyChat> applyChatQuery = criteriaBuilder.createQuery(ApplyChat.class);
					Join<MemberInfo,ApplyChat> join = root.join("memberInfo",JoinType.INNER);
					
					applyChatQuery.select(join).where(criteriaBuilder.equal(root.get("roundSerialNo"), roundSerialNo));
					return applyChatQuery.getRestriction();
				}
	    	};
	    	
	    	List<ApplyChat> applyChatList = applychatRepository.findAll(specification,new Sort(Direction.DESC,"sortOrder"));
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
  
/*  public DataResult<List> searchChatInformation(String roundSerialNo) {
    try {
      String sql = null;
      List<Map<String, Object>> list = null;
      sql = "select ApplyChat.*, MemberInfo.NickName from ApplyChat inner join MemberInfo on MemberInfo.MemberID = ApplyChat.MemberID  where RoundSerialNo= %s order by ApplyChat.SortOrder";
      sql = String.format(sql, nullToString(roundSerialNo, false));
      logger.debug("search ApplyChat sql:==" + sql);
      list = jdbcTemplate.queryForList(sql);
      return new DataResult<List>(list);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new DataResult<List>(ResultCode.NG.code(), "チャット情報が取得しません。");
    }
  }*/

  private static String nullToString(String target, boolean isString) {
    if (StringUtils.isEmpty(target)) {
      return null;
    }
    //return isString ? "'" + target + "'" : target;
    return target;
  }

}
